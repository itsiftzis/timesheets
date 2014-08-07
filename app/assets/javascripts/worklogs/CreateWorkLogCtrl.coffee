
class CreateWorkLogCtrl

    constructor: (@$log, @$location,  @WorkLogService, @$scope, @$routeParams) ->
        @$log.debug "constructing CreateWorkLogController"
        @worklog = {}
        @controlNumberOfInputRows = []
        @projects = [];
        @worklogEdit = []
        if @$routeParams.worklog
          @getObjectFromUrl(@$routeParams)


    getObjectFromUrl: (@$routeParams) ->
        @WorkLogService.fetchWorklog(@$routeParams)
        .then(
          (data) =>
            @$log.debug "Promise returned #{data} WorkLog"
            @worklogEdit.worklog = data
            @$log.debug @worklogEdit.worklog
        ,
        (error) =>
          @$log.error "Unable to create worklog: #{error}"
        )

    createWorkLog: () ->
        @$log.debug "createWorkLog()"
        @worklog.projects = @projects
        ###@worklog.dateLog = new Date(@worklog.dateLog).getTime()###
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

    editWorkLog: () ->
        @$log.debug "editWorkLog()"
        @$log.debug @worklogEdit.worklog
        @WorkLogService.updateWorkLog(@worklogEdit.worklog)
          .then(
            (data) =>
              @$log.debug "Promise returned #{data} "
              @$location.path("/")
          ,
          (error) =>
            @$log.error "Unable to update worklog: #{error}"
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
    addInputRowEdit: () ->
        @worklogEdit.worklog.projects.push({name:"", region:"", hours:""})
    deleteThisEdit: (st) ->
        @worklogEdit.worklog.projects.splice(st,1)

controllersModule2.controller('CreateWorkLogCtrl', CreateWorkLogCtrl)
