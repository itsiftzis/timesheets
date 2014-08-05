class ProjectService

    @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
    @defaultConfig = { headers: @headers }

    constructor: (@$log, @$http, @$q) ->
        @$log.debug "constructing ProjectService"

    listProjects: () ->
        @$log.debug "listProjects()"
        deferred = @$q.defer()

        @$http.get("/projects")
        .success((data, status, headers) =>
                @$log.info("Successfully listed Projects - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to list Projects - status #{status}")
                deferred.reject(data);
            )
        deferred.promise

    createProject: (project) ->
        @$log.debug "createProject #{angular.toJson(project, true)}"
        deferred = @$q.defer()

        @$http.post('/project', project)
        .success((data, status, headers) =>
                @$log.info("Successfully created Project - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to create Project - status #{status}")
                deferred.reject(data);
            )
        deferred.promise

servicesModule3.service('ProjectService', ProjectService)