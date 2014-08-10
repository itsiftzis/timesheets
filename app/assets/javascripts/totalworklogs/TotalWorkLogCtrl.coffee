
class TotalWorkLogCtrl

    constructor: (@$log, @TotalWorkLogService, @$scope) ->
        @$log.debug "constructing TotalWorkLogController"
        @worklogs = []
        @getAllWorkLogs()

    getAllWorkLogs: () ->
        @$log.debug "getAllWorkLogs()"

        @TotalWorkLogService.listWorkLogs()
        .then(
            (data) =>
                @$log.debug "Promise returned #{data.length} TotalWorkLogs"
                @worklogs = data
            ,
            (error) =>
                @$log.error "Unable to get WorkLogs: #{error}"
            )

    formatDate: (@date) ->
      tempDate = new Date(@date)
      return tempDate.getDate() + '/' + (tempDate.getMonth()+1) + '/' + tempDate.getFullYear()

    leadingZero: (value) ->
      if(value < 10)
        return "0" + value.toString()
      else
        return value.toString()

controllersModule3.controller('TotalWorkLogCtrl', TotalWorkLogCtrl)