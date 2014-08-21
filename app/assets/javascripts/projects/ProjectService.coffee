class ProjectService

    @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
    @defaultConfig = { headers: @headers }

    constructor: (@$log, @$http, @$q) ->
        @$log.debug "constructing ProjectService"

    listProjectClients: () ->
        @$log.debug "listProjectClients()"
        deferred = @$q.defer()

        @$http.get("/projectclients")
        .success((data, status, headers) =>
                @$log.info("Successfully listed Projects - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to list Projects - status #{status}")
                deferred.reject(data);
            )
        deferred.promise

    listProjectNames: () ->
        @$log.debug "listProjectNames()"
        deferred = @$q.defer()

        @$http.get("/projectnames")
        .success((data, status, headers) =>
              @$log.info("Successfully listed Projects - status #{status}")
              deferred.resolve(data)
        )
        .error((data, status, headers) =>
              @$log.error("Failed to list Projects - status #{status}")
              deferred.reject(data);
        )
        deferred.promise

    listProjectComponents: () ->
        @$log.debug "listProjectComponents()"
        deferred = @$q.defer()

        @$http.get("/projectcomponents")
        .success((data, status, headers) =>
          @$log.info("Successfully listed Projects - status #{status}")
          deferred.resolve(data)
        )
        .error((data, status, headers) =>
          @$log.error("Failed to list Projects - status #{status}")
          deferred.reject(data);
        )
        deferred.promise

    createProjectClient: (projectclient) ->
        @$log.debug "createProject #{angular.toJson(projectclient, true)}"
        @$log.debug projectclient
        deferred = @$q.defer()

        @$http.post('/projectclient', projectclient)
        .success((data, status, headers) =>
                @$log.info("Successfully created Project Client - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to create Project - status #{status}")
                deferred.reject(data);
            )
        deferred.promise

    createProjectName: (projectname) ->
        @$log.debug "createProject #{angular.toJson(projectname, true)}"
        @$log.debug projectname
        deferred = @$q.defer()

        @$http.post('/projectname', projectname)
        .success((data, status, headers) =>
                @$log.info("Successfully created Project Name - status #{status}")
                deferred.resolve(data)
        )
        .error((data, status, headers) =>
                @$log.error("Failed to create Project - status #{status}")
                deferred.reject(data);
        )
        deferred.promise

    createProjectComponent: (projectcomponent) ->
        @$log.debug "createProject #{angular.toJson(projectcomponent, true)}"
        deferred = @$q.defer()

        @$http.post('/projectcomponent', projectcomponent)
        .success((data, status, headers) =>
          @$log.info("Successfully created Project Component - status #{status}")
          deferred.resolve(data)
        )
        .error((data, status, headers) =>
          @$log.error("Failed to create Project - status #{status}")
          deferred.reject(data);
        )
        deferred.promise

    deleteProjectClient: (prj) ->
      deferred = @$q.defer()

      @$http.post('/deleteProjectClient', prj)
      .success((data, status, headers) =>
          @$log.info("Successfully deleted Project client - status #{status}")
          deferred.resolve(data)
        )
      .error((data, status, headers) =>
          @$log.error("Failed to delete Project client - status #{status}")
          deferred.reject(data);
        )
      deferred.promise

    deleteProjectName: (prj) ->
      deferred = @$q.defer()

      @$http.post('/deleteProjectName', prj)
      .success((data, status, headers) =>
          @$log.info("Successfully deleted Project name - status #{status}")
          deferred.resolve(data)
        )
      .error((data, status, headers) =>
          @$log.error("Failed to delete Project name - status #{status}")
          deferred.reject(data);
        )
      deferred.promise

    deleteProjectComponent: (prj) ->
      deferred = @$q.defer()

      @$http.post('/deleteProjectComponent', prj)
      .success((data, status, headers) =>
          @$log.info("Successfully deleted Project comp - status #{status}")
          deferred.resolve(data)
        )
      .error((data, status, headers) =>
          @$log.error("Failed to delete Project comp - status #{status}")
          deferred.reject(data);
        )
      deferred.promise

    fetchNamesForClient: (client) ->
      deferred = @$q.defer()

      @$http.post('/fetchNamesForClient', client)
      .success((data, status, headers) =>
          @$log.info("Successfully fetched Project names - status #{status}")
          deferred.resolve(data)
        )
      .error((data, status, headers) =>
          @$log.error("Failed to delete Project name - status #{status}")
          deferred.reject(data);
        )
      deferred.promise

servicesModule4.service('ProjectService', ProjectService)