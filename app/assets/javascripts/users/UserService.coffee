
class UserService

    @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
    @defaultConfig = { headers: @headers }

    constructor: (@$log, @$http, @$q) ->
        @$log.debug "constructing UserService"

    listUsers: () ->
        @$log.debug "listUsers()"
        deferred = @$q.defer()

        @$http.get("/users")
        .success((data, status, headers) =>
                @$log.info("Successfully listed Users - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to list Users - status #{status}")
                deferred.reject(data);
            )
        deferred.promise

    createUser: (user) ->
        @$log.debug "createUser #{angular.toJson(user, true)}"
        deferred = @$q.defer()

        @$http.post('/user', user)
        .success((data, status, headers) =>
                @$log.info("Successfully created User - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to create user - status #{status}")
                deferred.reject(data);
            )
        deferred.promise

    updateUser: (data, id) ->
      deferred = @$q.defer()

      @$http.post('/updateUser', data)
      .success((data, status, headers) =>
        @$log.info("Successfully updated User - status #{status}")
        deferred.resolve(data)
      )
      .error((data, status, headers) =>
        @$log.error("Failed to update user - status #{status}")
        deferred.reject(data);
      )
      deferred.promise

    deleteUser: (usr) ->
      deferred = @$q.defer()

      @$http.post('/deleteUser', usr)
      .success((data, status, headers) =>
        @$log.info("Successfully deleted User - status #{status}")
        deferred.resolve(data)
      )
      .error((data, status, headers) =>
        @$log.error("Failed to delete user - status #{status}")
        deferred.reject(data);
      )
      deferred.promise

servicesModule.service('UserService', UserService)