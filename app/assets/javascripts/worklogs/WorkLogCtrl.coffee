
class WorkLogCtrl

    constructor: (@$log, @WorkLogService) ->
        @$log.debug "constructing WorkLogController"
        @worklogs = []
        @getAllWorkLogs()

    getAllWorkLogs: () ->
        @$log.debug "getAllWorkLogs()"

        @WorkLogService.listWorkLogs()
        .then(
            (data) =>
                @$log.debug "Promise returned #{data.length} WorkLogs"
                @worklogs = data
            ,
            (error) =>
                @$log.error "Unable to get WorkLogs: #{error}"
            )

controllersModule2.controller('WorkLogCtrl', WorkLogCtrl)