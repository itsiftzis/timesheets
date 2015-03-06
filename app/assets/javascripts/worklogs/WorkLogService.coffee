
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

    getMissingHours: (date) ->
        @$log.debug "getMissingHours()"
        deferred = @$q.defer()

        @$http.get("/hoursPerMonth/" + date)
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

    batchWorkLog: (worklog) ->
      @$log.debug "batchWorkLog #{angular.toJson(worklog, true)}"
      deferred = @$q.defer()

      @$http.post('/batchworklog', worklog)
      .success((data, status, headers) =>
        @$log.info("Successfully created batchworklog - status #{status}")
        deferred.resolve(data)
      )
      .error((data, status, headers) =>
        @$log.error("Failed to create batch worklog - status #{status}")
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

    getTeamRecentWorklogs: () ->
        @$log.debug "getTeamRecentWorklogs()"
        deferred = @$q.defer()

        @$http.get("/allRecentWorklogs/5")
        .success((data, status, headers) =>
          @$log.info("Successfully listed teams Projects - status #{status}")
          deferred.resolve(data)
        )
        .error((data, status, headers) =>
          @$log.error("Failed to list teams Projects - status #{status}")
          deferred.reject(data);
        )
        deferred.promise

    getMyRecentWorklogs: () ->
        @$log.debug "getMyRecentWorklogs()"
        deferred = @$q.defer()

        @$http.get("/recentWorklogs/5")
        .success((data, status, headers) =>
          @$log.info("Successfully listed Projects - status #{status}")
          deferred.resolve(data)
        )
        .error((data, status, headers) =>
          @$log.error("Failed to list Projects - status #{status}")
          deferred.reject(data);
        )
        deferred.promise

    ###myFrequentWorklogs###
    myFrequentWorklogs: () ->
        @$log.debug "myFrequentWorklogs()"
        deferred = @$q.defer()

        @$http.get("/frequentWorklogs/5")
        .success((data, status, headers) =>
          @$log.info("Successfully listed Projects - status #{status}")
          deferred.resolve(data)
        )
        .error((data, status, headers) =>
          @$log.error("Failed to list Projects - status #{status}")
          deferred.reject(data);
        )
        deferred.promise

    ###myFrequentWorklogs###
    teamFrequentWorklogs: () ->
        @$log.debug "teamFrequentWorklogs()"
        deferred = @$q.defer()

        @$http.get("/teamFrequentWorklogs/5")
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

    fetchComponentsForName: (name) ->
      deferred = @$q.defer()

      @$http.post('/fetchComponentsForName', name)
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