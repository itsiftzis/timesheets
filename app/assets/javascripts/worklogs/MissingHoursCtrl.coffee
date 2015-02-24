
class MissingHoursCtrl

    constructor: (@$log, @MissingHoursService, @$scope) ->
        @$log.debug "constructing MissingHours Controller"
        @worklogs = []
        @getAllWorkLogs()
        @$scope.pr = []
        @curPage
        @pageSize
        @numberOfPages
        @missinghours
        @getMissingHours()

    getAllWorkLogs: () ->
        @$log.debug "getAllWorkLogs()"

        @MissingHoursService.listWorkLogs()
        .then(
            (data) =>
                @$log.debug "Promise returned #{data.length} WorkLogs"
                @worklogs = data
                @$scope.pr = data
                @curPage = 0
                @pageSize = 4
                @numberOfPages = Math.ceil(@worklogs.length / @pageSize)
                @$log.debug @numberOfPages
            ,
            (error) =>
                @$log.error "Unable to get WorkLogs: #{error}"
            )

    getMissingHours: () ->
        @$log.debug "getMissingHours()"

        @MissingHoursService.getMissingHours('all')
          .then(
              (data) =>
                @$log.debug "Promise returned #{data.length} WorkLogs"
                @missinghours = data.totalHours
          ,
            (error) =>
              @$log.error "Unable to get WorkLogs: #{error}"
          )

    formatDate: (@date) ->
        tempDate = new Date(@date)
        return tempDate.getDate() + '/' + (tempDate.getMonth()+1) + '/' + tempDate.getFullYear()

    fixDateFormat: (@tempWorklog) ->
        tempDate = new Date(tempWorklog.dateLog)
        @tempWorklog.dateLog = tempDate.getFullYear() + '-' + this.leadingZero(tempDate.getMonth()) + '-' + this.leadingZero(tempDate.getDate()) +
          'T' + tempDate.getHours() + ':' + tempDate.getMinutes() + ':' + tempDate.getSeconds() + '.' + tempDate.getMilliseconds() + 'Z'
        ###2014-07-07T00:00:00.000Z###
        return @tempWorklog

    leadingZero: (value) ->
        if(value < 10)
          return "0" + value.toString()
        else
          return value.toString()


controllersModule5.controller('MissingHoursCtrl', MissingHoursCtrl)