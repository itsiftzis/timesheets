
class CreateWorkLogCtrl

    constructor: (@$log, @$location,  @WorkLogService, @$scope, @$routeParams) ->
        @$log.debug "constructing CreateWorkLogController"
        @worklog = {}
        @controlNumberOfInputRows = []
        @projects = [];
        @worklogEdit = []
        if @$routeParams.worklog
          @getObjectFromUrl(@$routeParams)
        @prclients = {}
        @prnames = {}
        @prcomps = {}
        @prccurrentclient = {}
        @prcurrentname = {}
        @fetchProjectClients()

    open: ($event) ->
        $event.preventDefault();
        $event.stopPropagation();
        @$scope.opened = true;

    fetchProjectClients: () ->
      @$log.debug "fetchProjectClients()"
      @WorkLogService.listProjectClients()
      .then(
          (data) =>
            @$log.debug "Promise returned #{data} Project"
            @prclients = data
        ,
        (error) =>
          @$log.error "Unable to fetch Project Clients: #{error}"
        )

    notEmpty: (@thi) ->
      @$log.debug(@thi)

    fillNames: (@name, @index) ->
      @$log.debug @name
      if (@name != undefined )
        @WorkLogService.fetchNamesForClient(@name)
        .then(
            (data) =>
              @$log.debug "Promise returned #{data} Project"
              @prnames[@index] = data
          ,
          (error) =>
            @$log.error "Unable to get Project names: #{error}"
          )

    fillComponents: (@name,@index) ->
      @$log.debug @name
      if (@name != undefined )
        @WorkLogService.fetchComponentsForName(@name,)
        .then(
            (data) =>
              @$log.debug "Promise returned #{data} Project"
              @prcomps[@index] = data
          ,
          (error) =>
            @$log.error "Unable to get Project names: #{error}"
          )

    getObjectFromUrl: (@$routeParams) ->
        @WorkLogService.fetchWorklog(@$routeParams)
        .then(
          (data) =>
            @$log.debug "Promise returned #{data} WorkLog"
            tempDate = new Date(Date.parse(data.dateLog))
            data.dateLog = tempDate
            @worklogEdit.worklog = data
            @$log.debug @worklogEdit.worklog
        ,
        (error) =>
          @$log.error "Unable to create worklog: #{error}"
        )

    createWorkLog: () ->
        @$log.debug "createWorkLog()"

        for pr in @projects
          @$log.debug pr
          pr.client = pr.client.client
          pr.name = pr.name.name
          pr.component = pr.component.component
        @worklog.dateLog.setHours(18)
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
        @projects.push({client:"", name:"", component:"", region:"", hours:""})
        console.log(@projects)
    deleteThis: (st) ->
        @controlNumberOfInputRows.splice(st,1)
        @projects.splice(st,1)
    addInputRowEdit: () ->
        @worklogEdit.worklog.projects.push({client:"", name:"", component:"", region:"", hours:""})
    deleteThisEdit: (st) ->
        @worklogEdit.worklog.projects.splice(st,1)

controllersModule2.controller('CreateWorkLogCtrl', CreateWorkLogCtrl)
