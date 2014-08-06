
class WorkLogCtrl

    constructor: (@$log, @WorkLogService, @$scope) ->
        @$log.debug "constructing WorkLogController"
        @worklogs = []
        @getAllWorkLogs()
        @$scope.pr = []

    getAllWorkLogs: () ->
        @$log.debug "getAllWorkLogs()"

        @WorkLogService.listWorkLogs()
        .then(
            (data) =>
                @$log.debug "Promise returned #{data.length} WorkLogs"
                @worklogs = data
                @$scope.pr = data
            ,
            (error) =>
                @$log.error "Unable to get WorkLogs: #{error}"
            )

    saveProject: (@pr) ->
      @$log.debug "saveProject()"
      @$log.debug @pr

    saveWorklog: (@data, @id) ->
      @$log.debug "saveWorkLog()"
      @$log.debug @data
      @$log.debug @id

    removeWorkLog: (@wrl, @index) ->
      @worklogs.splice(@index, 1)
      @WorkLogService.deleteWorkLog(@wrl)
      .then(
        (data) =>
          @$log.debug "Promise returned #{data} "
      ,
      (error) =>
        @$log.error "Unable to delete Worklog: #{error}"
      )

    editWorklog: (@wl) ->
      @$log.debug(@wl)

controllersModule2.controller('WorkLogCtrl', WorkLogCtrl)