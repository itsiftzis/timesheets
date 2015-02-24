
class MissingHoursService

    @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
    @defaultConfig = { headers: @headers }

    constructor: (@$log, @$http, @$q) ->
        @$log.debug "constructing MissingHoursService"

    listWorkLogs: () ->
        @$log.debug "listWorkLogs()"
        deferred = @$q.defer()

        @$http.get("/missinghourlogs")
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

servicesModule5.service('MissingHoursService', MissingHoursService)