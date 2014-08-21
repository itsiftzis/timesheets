
class TotalWorkLogService

    @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
    @defaultConfig = { headers: @headers }

    constructor: (@$log, @$http, @$q) ->
        @$log.debug "constructing WorkLogService"

    listWorkLogs: () ->
        @$log.debug "listWorkLogs()"
        deferred = @$q.defer()

        @$http.get("/totalworklogs")
        .success((data, status, headers) =>
                @$log.info("Successfully listed WorkLogs - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to list WorkLogs - status #{status}")
                deferred.reject(data);
            )
        deferred.promise

    download: (jsonData) ->
        @$log.debug "listWorkLogs()"
        deferred = @$q.defer()

        @$http.post("/serveJson", jsonData)
        .success((data, status, headers) =>
          @$log.info("Successfully served total TotalWorklogs - status #{status}")
          deferred.resolve(data)
        )
        .error((data, status, headers) =>
          @$log.error("Failed to serve total workklogs - status #{status}")
          deferred.reject(data);
        )
        deferred.promise

servicesModule3.service('TotalWorkLogService', TotalWorkLogService)