mongo admin --eval "db.system.users.remove({})"
mongo admin --eval "db.createUser({user: 'admin',pwd: 'admin',roles: [ { role: 'userAdminAnyDatabase', db: 'admin' } ]})"