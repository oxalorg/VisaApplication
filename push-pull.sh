#!/usr/bin/sh
git add .
git commit -m "RAMBO"
git push server master
ssh -t mitu@server1.mitesh.ninja "cd /var/www/server1.miteshshah.com/public && git pull server master"
