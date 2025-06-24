start "Terminal" cmd /k "title CLEANUP-DOCKER && for /F %i in ('docker ps -a -q') do docker rm -f %i"
