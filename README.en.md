# cy_comment_backen

#### Description
社区后端

#### Software Architecture
Software architecture description

#### Installation

1.  xxxx
2.  xxxx
3.  xxxx

#### Instructions

1.  xxxx
2.  xxxx
3.  xxxx

#### Contribution

1.  Fork the repository
2.  Create Feat_xxx branch
3.  Commit your code
4.  Create Pull Request


#### Gitee Feature

1.  You can use Readme\_XXX.md to support different languages, such as Readme\_en.md, Readme\_zh.md
2.  Gitee blog [blog.gitee.com](https://blog.gitee.com)
3.  Explore open source project [https://gitee.com/explore](https://gitee.com/explore)
4.  The most valuable open source project [GVP](https://gitee.com/gvp)
5.  The manual of Gitee [https://gitee.com/help](https://gitee.com/help)
6.  The most popular members  [https://gitee.com/gitee-stars/](https://gitee.com/gitee-stars/)


# 部署
### 安装docker
1. 更新yum包
```shell
sudo yum update
```
2. 安装所需要的包
```shell
sudo yum install -y yum-utils device-mapper-persistent-data lvm2
```
3. 设置yum源为阿里云
```shell
sudo yum-config-manager --add-repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
```
4. 安装Docker,去docker-hub网站查找版本
```shell
sudo yum install docker
```
5. 测试安装成功
```shell
docker version
```
6. 利用阿里云加速

登录阿里云->控制台->容器服务->容器镜像服务->镜像工具->镜像加速器
```shell
sudo mkdir -p /etc/docker
sudo tee /etc/docker/daemon.json <<-'EOF'
{
  "registry-mirrors": ["https://xxxxxx.mirror.aliyuncs.com"]
}
EOF
sudo systemctl daemon-reload
sudo systemctl restart docker
```
### 安装mysql
1. 寻找镜像在docker hub
2. 下载
```shell
docker pull mysql
```
3. 查看
```shell
docker images
```
4. 运行
```shell
docker run -d -p 3306:3306 --name mysql -e MYSQL_ROOT_PASSWORD=xxxxxx mysql
```
-p:设置端口，此处为映射，将容器3306端口映射到宿主机的3306端口，阿里服务器得在安全组添加规则

-d:表示作为一个守护进程，在后台运行

--name:取名

-e MYSQL_ROOT_PASSWORD=xxxxxx:表示登陆账号为root，密码为xxxxxx

最后一个mysql:表示容器名，最好加上tag,没加好像默认latest
5. 查看
```shell
docker ps -a
```
6. 进入mysql容器
```shell
docker exec -it mysql /bin/bash
```

### 使用docker部署
#### Dockerfile
```shell
FROM openjdk:8-jre-alpine  #jre基础环境

# 维护者信息
MAINTAINER zhengqingya

COPY *.jar /app.jar        # 添加jar包到容器中

CMD ["--server.port=8000"]

EXPOSE 8000                        # 对外暴漏的端口号

ENTRYPOINT ["java","-jar","/app.jar"]      # RUN🏃🏃
```
#### 打包
package
#### 部署
将Dockerfile和.jar文件复制到一个目录下

打包镜像-f:指定Dockerfile文件路径 --no-cache:构建镜像时不使用缓存
```shell
docker build -f Dockerfile -t "springboot" . --no-cache
```
运行
```shell
docker run -d -p 3001:8080 -v /myProject/app.log:/home/app.log --name springboot springboot
```
删除旧容器
```shell
docker rm -f springboot
```
删除旧镜像
```shell
docker rmi -f springboot
```







