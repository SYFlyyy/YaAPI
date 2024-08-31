import {useModel} from '@@/exports';
import {
  FieldTimeOutlined, IdcardOutlined,
  LockOutlined, MessageOutlined,
  UnlockOutlined,
  UserOutlined,
  VerifiedOutlined,
} from '@ant-design/icons';
import {ModalForm, PageContainer, ProForm, ProFormInstance, ProFormText} from '@ant-design/pro-components';
import {Button, Card, Col, Divider, message, Modal, Row, Typography} from 'antd';
import React, {useEffect, useRef, useState} from 'react';
import {
  getUserVoByIdUsingGet,
  updateSecretKeyUsingPost, updateUserUsingPost,
  userLoginUsingPost
} from "@/services/yaapi-backend/userController";
import moment from "moment";

const { Paragraph } = Typography;

const avatarStyle: React.CSSProperties = {
  width: '100%',
  textAlign: 'center',
};
const buttonStyle: React.CSSProperties = {
  marginLeft: '30px',
};

const Profile: React.FC = () => {
  const [data, setData] = useState<API.UserVO>({});
  const [visible, setVisible] = useState<boolean>(false);
  const [flag, setFlag] = useState<boolean>(false);
  const [open, setOpen] = useState<boolean>(false);
  const [, setImageUrl] = useState<string>();
  const { initialState, setInitialState } = useModel('@@initialState');
  const [modalTitle, setModalTitle] = useState<string>();
  const formRef = useRef<
    ProFormInstance<{
      userPassword: string;
    }>
  >();

  // 获取用户信息
  const getUserInfo = async (id: any) => {
    return getUserVoByIdUsingGet({ id }).then((res) => {
      if (res.data) {
        setInitialState((s: any) => ({ ...s, loginUser: res.data }));
        setData(res.data);
        setImageUrl(res.data.userAvatar);
      }
    });
  };

  useEffect(() => {
    try {
      getUserInfo(initialState?.loginUser?.id);
    } catch (e: any) {
      console.log(e);
    }
  }, []);

  // 显示秘钥
  const showSecretKey = async () => {
    let userPassword = formRef?.current?.getFieldValue('userPassword' as any);
    // 登录
    const res = await userLoginUsingPost({
      userAccount: data?.userAccount,
      userPassword: userPassword,
    });
    if (res.code === 0) {
      setOpen(false);
      setVisible(true);
      formRef?.current?.resetFields();
    }
  };

  // 重置秘钥
  const resetSecretKey = async () => {
    try {
      let userPassword = formRef?.current?.getFieldValue('userPassword' as any);
      // 登录
      const res = await userLoginUsingPost({
        userAccount: data?.userAccount,
        userPassword: userPassword,
      });
      if (res.code === 0) {
        const res = await updateSecretKeyUsingPost({
          id: data?.id,
        });
        if (res.data) {
          getUserInfo(data?.id);
          message.success('重置成功！');
          setOpen(false);
        }
      }
    } catch (e: any) {
      console.log(e);
    }
  };

  // 修改用户信息
  const updateUserInfo = async (fields: API.UserUpdateRequest) => {
    const hide = message.loading('修改中');
    try {
      await updateUserUsingPost({
        id: data?.id,
        ...fields,
      });
      hide();
      message.success('修改成功');
      window.location.reload();
      return true;
    } catch (error: any) {
      hide();
      message.error('修改失败，' + error.message);
      return false;
    }
  };

  return (
    <PageContainer>
      <Row gutter={24}>
        <Col span={8}>
          <Card title="个人信息" bordered={false}>
            <Row>
              <Col style={avatarStyle}>
                <img
                  src={data?.userAvatar}
                  alt="avatar"
                  style={{ width: '30%', borderRadius: '50%' }}
                />
              </Col>
            </Row>
            <Divider />
            <Row>
              <Col>
                <UserOutlined /> 用户昵称：{data?.userName}
                <ModalForm
                  title="修改昵称"
                  trigger={
                    <Typography.Text  editable={{onChange: () => {}}}/>
                  }
                  submitter={{
                    searchConfig: {
                      submitText: '确认',
                      resetText: '取消',
                    },
                  }}
                  onFinish={async (values) => {
                    await updateUserInfo(values);
                    return true;
                  }}
                  modalProps={{
                    width: 400,
                    afterClose: () => formRef.current?.resetFields(),
                  }}
                  formRef={formRef}
                >
                  <div style={{ height: '20px' }} />
                  <ProFormText width="md" name="userName" placeholder="请输入新昵称" />
                </ModalForm>
              </Col>
            </Row>
            <Divider />
            <Row>
              <Col>
                <IdcardOutlined /> 用户账号：{data?.userAccount}
              </Col>
            </Row>
            <Divider />
            <Row>
              <Col>
                <MessageOutlined /> 个人简介：{data?.userProfile}
                <ModalForm
                  title="修改个人简介"
                  trigger={
                    <Typography.Text  editable={{onChange: () => {}}}/>
                  }
                  submitter={{
                    searchConfig: {
                      submitText: '确认',
                      resetText: '取消',
                    },
                  }}
                  onFinish={async (values) => {
                    await updateUserInfo(values);
                    return true;
                  }}
                  modalProps={{
                    width: 400,
                    afterClose: () => formRef.current?.resetFields(),
                  }}
                  formRef={formRef}
                >
                  <div style={{ height: '20px' }} />
                  <ProFormText
                    width="md"
                    name="userProfile"
                    placeholder="快来介绍一下自己"
                    rules={[
                      {
                        max: 15,
                        message: '不能超过15个字符',
                      },
                    ]}
                  />
                </ModalForm>
              </Col>
            </Row>
            <Divider />
            <Row>
              <Col>
                <VerifiedOutlined /> 用户角色：{data?.userRole}
              </Col>
            </Row>
            <Divider />
            <Row>
              <Col>
                <FieldTimeOutlined /> 注册时间：
                {moment(data?.createTime).format('YYYY-MM-DD HH:mm:ss')}
              </Col>
            </Row>
          </Card>
        </Col>
        <Col span={16}>
          <Card title="秘钥操作" bordered={false}>
            <Row>
              <Col>
                {visible ? (
                  <Paragraph
                    copyable={{
                      text: data?.accessKey,
                    }}
                  >
                    <LockOutlined /> accessKey：{data?.accessKey}
                  </Paragraph>
                ) : (
                  <Paragraph>
                    <UnlockOutlined /> secretKey：*********
                  </Paragraph>
                )}
              </Col>
            </Row>
            <Divider />
            <Row>
              <Col>
                {visible ? (
                  <Paragraph
                    copyable={{
                      text: data?.secretKey,
                    }}
                  >
                    <UnlockOutlined /> secretKey：{data?.secretKey}
                  </Paragraph>
                ) : (
                  <Paragraph>
                    <UnlockOutlined /> secretKey：*********
                  </Paragraph>
                )}
              </Col>
            </Row>
            <Divider />
            <Row>
              <Col>
                {!visible ? (
                  <Button
                    type="primary"
                    onClick={() => {
                      setModalTitle('查看秘钥');
                      setOpen(true);
                      setFlag(true);
                    }}
                  >
                    查看秘钥
                  </Button>
                ) : (
                  <Button type="primary" onClick={() => setVisible(false)}>
                    隐藏秘钥
                  </Button>
                )}
                <Button
                  style={buttonStyle}
                  onClick={() => {
                    setModalTitle('重置秘钥');
                    setOpen(true);
                    setFlag(false);
                  }}
                  type="primary"
                  danger
                >
                  重置秘钥
                </Button>
              </Col>
            </Row>
          </Card>
        </Col>
      </Row>
      <Modal
        title={modalTitle}
        open={open}
        onOk={flag ? showSecretKey : resetSecretKey}
        onCancel={() => setOpen(false)}
        width={400}
      >
        <ProForm<{
          userPassword: string;
        }>
          formRef={formRef}
          formKey="check-user-password-form"
          autoFocusFirstInput
          submitter={{
            resetButtonProps: {
              style: {
                display: 'none',
              },
            },
            submitButtonProps: {
              style: {
                display: 'none',
              },
            },
          }}
        >
          <div style={{ height: '20px' }} />
          <ProFormText.Password width="md" name="userPassword" placeholder="请输入用户密码" />
        </ProForm>
      </Modal>
    </PageContainer>
  );
};

export default Profile;
