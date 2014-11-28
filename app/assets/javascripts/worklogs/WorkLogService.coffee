
class WorkLogService

    @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
    @defaultConfig = { headers: @headers }

    constructor: (@$log, @$http, @$q) ->
        @$log.debug "constructing WorkLogService"

    listWorkLogs: () ->
        @$log.debug "listWorkLogs()"
        deferred = @$q.defer()

        @$http.get("/worklogs")
        .success((data, status, headers) =>
                @$log.info("Successfully listed WorkLogs - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to list WorkLogs - status #{status}")
                deferred.reject(data);
            )
        deferred.promise

    fetchWorklog: (id) ->
        @$log.debug "fetchWorklog()" + id.worklog
        deferred = @$q.defer()

        @$http.get("/fetchworklog/" + id.worklog)
        .success((data, status, headers) =>
          @$log.info("Successfully fetched WorkLog - status #{status}")
          deferred.resolve(data)
        )
        .error((data, status, headers) =>
          @$log.error("Failed to list Worklog - status #{status}")
          deferred.reject(data);
        )
        deferred.promise

    updateWorkLog: (data) ->
        deferred = @$q.defer()
        @$http.post('/updateWorkLog', data)
        .success((data, status, headers) =>
          @$log.info("Successfully updated Worklog - status #{status}")
          deferred.resolve(data)
        )
        .error((data, status, headers) =>
          @$log.error("Failed to update worklog - status #{status}")
          deferred.reject(data);
        )
        deferred.promise

    createWorkLog: (worklog) ->
        @$log.debug "createWorkLog #{angular.toJson(worklog, true)}"
        deferred = @$q.defer()

        @$http.post('/worklog', worklog)
        .success((data, status, headers) =>
                @$log.info("Successfully created worklog - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to create worklog - status #{status}")
                deferred.reject(data);
            )
        deferred.promise

    deleteWorkLog: (worklog) ->
      @$log.debug "deleteWorklog #{angular.toJson(worklog, true)}"
      deferred = @$q.defer()
      @$log.debug " {\"id\": \"" + worklog.id + "\" }"
      @$http.post('/deleteWorklog', '{"id":"' + worklog.id + '"}')
      .success((data, status, headers) =>
        @$log.info("Successfully deleted worklog - status #{status}")
        deferred.resolve(data)
      )
      .error((data, status, headers) =>
        @$log.error("Failed to delete worklog - status #{status}")
        deferred.reject(data);
      )
      deferred.promise

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

    fetchComponentsForName: (name, param2) ->
      deferred = @$q.defer()

      @$http.post('/fetchComponentsForName', name, param2)
      .success((data, status, headers) =>
          @$log.info("Successfully fetched Project components - status #{status}")
          deferred.resolve(data)
        )
      .error((data, status, headers) =>
          @$log.error("Failed to delete Project components - status #{status}")
          deferred.reject(data);
        )
      deferred.promise

servicesModule2.service('WorkLogService', WorkLogService)