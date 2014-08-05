
class CreateWorkLogCtrl

    constructor: (@$log, @$location,  @WorkLogService, @$scope) ->
        @$log.debug "constructing CreateWorkLogController"
        @worklog = {}
        @controlNumberOfInputRows = []
        @projects = [];

    createWorkLog: () ->
        @$log.debug "createWorkLog()"
        for key, val of @projects
            console.log key + ': ' + val

        @worklog.projects = @projects
        @WorkLogService.createWorkLog(@worklog)
        .then(
            (data) =>
                @$log.debug "Promise returned #{data} WorkLog"
                @worklog = data
                @$location.path("/")
            ,
            (error) =>
                @$log.error "Unable to create worklog: #{error}"
            )

    addFormField: ($scope) ->
      $scope.projects.fields.push('')
    submitTable: ($scope) ->
      console.log($scope.projects)

    addInputRow: () ->
        @controlNumberOfInputRows.push(0)
        @projects.push({name:"", region:"", hours:""})
        console.log(@projects)
    deleteThis: (st) ->
        @controlNumberOfInputRows.splice(st,1)

controllersModule2.controller('CreateWorkLogCtrl', CreateWorkLogCtrl)
