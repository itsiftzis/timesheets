
class UserCtrl

    constructor: (@$log, @UserService, $http) ->
        @$log.debug "constructing UserController"
        @users = []
        @getAllUsers()

    getAllUsers: () ->
        @$log.debug "getAllUsers()"

        @UserService.listUsers()
        .then(
            (data) =>
                @$log.debug "Promise returned #{data.length} Users"
                @users = data
            ,
            (error) =>
                @$log.error "Unable to get Users: #{error}"
            )

    saveUser: (@data, @id) ->
      console.log(@data)
      console.log(@id)
      angular.extend(data, {id: @id})
      @UserService.updateUser(@data, @id)
      .then(
        (data) =>
          @$log.debug "Promise returned #{data} "
          ###@users = data###
      ,
      (error) =>
        @$log.error "Unable to update Users: #{error}"
      )

    removeUser: (@usr, @index) ->
      @users.splice(@index, 1)
      @UserService.deleteUser(@usr)
      .then(
        (data) =>
          @$log.debug "Promise returned #{data} "
          ###@users = data###
      ,
      (error) =>
        @$log.error "Unable to delete Users: #{error}"
      )

controllersModule.controller('UserCtrl', UserCtrl)