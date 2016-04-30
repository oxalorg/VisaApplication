server {
    listen 80;
    listen [::]:80;
    server_name server1.miteshshah.com www.server1.miteshshah.com;
    server_tokens off;

    root /var/www/server1.miteshshah.com/public;
    index index.html index.htm;

    location /static {
        alias /var/www/server1.miteshshah.com/public/visa/static;
        autoindex on;
    }

    location / {
        include uwsgi_params;
        uwsgi_pass unix:///tmp/server1.miteshshah.visa.sock;
    }

    error_page 404 /404.html;
}
