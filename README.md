# lightTrace

# 使用场景
应用整体响应时间长，但不知道方法是有多慢。一旦重启就再难复现。

# 使用方式
1. 进入lightTrace目录下，可以查看到有Run.bat
1. jps查看java进程id
1. Run.bat pid 要查看方法的类，例如Run.bat 30232 Bean 表示进程号为30232的进程，类名里包含bean的类输出方法的执行时间。


# 注意
1. 现在只提供了windows的脚本，如果linux就自己java执行吧
1. 需要有JAVA_HOME的环境变量
