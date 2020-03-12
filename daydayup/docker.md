###核心概念
	镜像
	
	容器

	镜像加速器
	
	仓库
	
	dockerfile
	
###账户
helunmingde@foxmail.com
helunmingde:mingde930720
	
###启动
docker run		构建并启动容器
	
	-t 在容器中启动一个伪终端，用于交互 
	-i 支持和容器内标准输入输出进行交互
	-d 后台启动容器
	-P 将容器内部使用的端口映射到宿主机上            -p 容器端口 ：宿主端口
	
	exit 退出容器
	
docker start   启动容器

### 使用
	
docker exec 重新进入容器内部  注意区别docker attach :attach退出后会导致容器停止
docker log  容器id或者容器名称 ：   在宿主机中使用 查看容器的标准输出
docker log -f 容器id或者容器名称  类似tail -f 可以输出容器的标准输出到宿主终端
docker top 容器id或者容器名称  ： 类似top  可以直接查看容器的进程
docker inspect 容器id或者容器名 ： 查看容器在docker中的底层配置 

### 容器 容器标识 容器id
	
docker export 
docker import 
docker rm 删除容器

### 镜像 镜像标识 仓库源:标签
docker pull 仓库源:标签  从远端下载镜像
docker push 仓库源:标签  将本地镜像推向远端仓库
docker images 列出本地主机已有的镜像
docker search keywords 搜索镜像
docker rmi	删除镜像

可以从 Docker Hub 网站来搜索镜像，Docker Hub 网址为： https://hub.docker.com/

镜像更新:
	1 拉去基本镜像
	2 启动一个镜像容器
	3 对容器做满足条件的更新
	4 使用docker commit -m 'message' -a 'author' 容器id 镜像名（仓库源）:版本（tag） 保存为一个新的镜像

创建镜像:
	1 书写dockerfile
	2 使用docker build -t 镜像名（仓库源）:版本（tag） dockerfile路径
	
添加标签：
	docker tag 镜像id 镜像名（仓库源）:版本（tag）
	
### 容器连接

1 端口映射

2 父子容器
	
	1 docker network -d bridge test-net 创建一个名为test-net的桥接网络
	2 docker run -itd --name test1 --network test-net ubuntu /bin/bash 启动一个名为test1的容器并连接到test-net网络
	3 docker run -itd --name test2 --network test-net ubuntu /bin/bash  另一个容器
	这样 在test1容器中可以直接ping通test2  也就将两个容器连接起来了。

### DNS配置


### dockerFile
自创建镜像配置文件

### dockercompose
定义和运行多个容器的工具

### dockermachine
虚拟机管理工具

### dockerswarm
集群管理工具


	
	
	