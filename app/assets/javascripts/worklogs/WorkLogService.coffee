
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

servicesModule2.service('WorkLogService', WorkLogService)