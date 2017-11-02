rem build project
rem activator clean
rem activator dist

rem upload
pscp.exe -pw _your_password C:\git.nexon.net\net.nexon.jupiter.api\play-scala-jupiter\target\universal\play-scala-jupiter-1.0.zip root@10.8.145.6:/home/

