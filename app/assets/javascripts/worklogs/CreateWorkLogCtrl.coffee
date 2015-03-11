
class CreateWorkLogCtrl

    constructor: (@$log, @$location,  @WorkLogService, @$scope, @$routeParams) ->
        @$log.debug "constructing CreateWorkLogController"
        @worklog = {}
        @controlNumberOfInputRows = []
        @projects = [];
        @projectsSelected = [];
        @worklogEdit = []
        if @$routeParams.worklog
          @getObjectFromUrl(@$routeParams)
        @prclients = {}
        @prnames = {}
        @prcomps = {}
        @prccurrentclient = {}
        @prcurrentname = {}
        @fetchProjectClients()
        @myRecentWorklogs = {}
        @getMyRecentWorklogs()
        @myFrequentWorklogs()
        @teamFrequentWorklogs()
        @teamRecentWorklogs = {}
        @getTeamRecentWorklogs()

    open: ($event) ->
        $event.preventDefault();
        $event.stopPropagation();
        @$scope.opened = true;

    getTeamRecentWorklogs: () ->
        @$log.debug "@getTeamRecentWorklogs()"
        @WorkLogService.getTeamRecentWorklogs()
        .then(
          (data) =>
            @$log.debug "Promise returned #{data} teams recent Projects"
            ###todo filter projects###
            @teamRecentWorklogs = data
        ,
          (error) =>
            @$log.error "Unable to fetch Project Clients: #{error}"
        )

    getMyRecentWorklogs: () ->
        @$log.debug "@getMyRecentWorklogs()"
        @WorkLogService.getMyRecentWorklogs()
        .then(
          (data) =>
            @$log.debug "Promise returned #{data} recent Project"
            ###todo filter projects###
            @myRecentWorklogs = data
        ,
          (error) =>
            @$log.error "Unable to fetch Project Clients: #{error}"
        )

    ###myFrequentWorklogs###
    myFrequentWorklogs: () ->
      @$log.debug "myFrequentWorklogs()"
      @WorkLogService.getMyFrequentWorklogs()
      .then(
        (data) =>
          @$log.debug "Promise returned #{data} frequent Project"
          @myFrequentWorklogs = data
      ,
        (error) =>
          @$log.error "Unable to fetch Project Clients: #{error}"
      )

    ###teamFrequentWorklogs###
    teamFrequentWorklogs: () ->
      @$log.debug "teamFrequentWorklogs()"
      @WorkLogService.getTeamFrequentWorklogs()
      .then(
        (data) =>
          @$log.debug "Promise returned #{data} frequent Project"
          @teamFrequentWorklogs = data
      ,
        (error) =>
          @$log.error "Unable to fetch Project Clients: #{error}"
      )

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
        for pr in @projectsSelected
          @worklog.projects.push pr
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
    deleteThisSelected: (st) ->
        @controlNumberOfInputRows.splice(st,1)
        @projectsSelected.splice(st,1)
    addInputRowEdit: () ->
        @worklogEdit.worklog.projects.push({client:"", name:"", component:"", region:"", hours:""})
    deleteThisEdit: (st) ->
        @worklogEdit.worklog.projects.splice(st,1)

    addworklog: (_client, _component, _name, _description, _hours) ->
        @$log.info(_client + ' ' + _component + ' ' + _name, + ' ' + _description, + ' ' + _hours)
        @controlNumberOfInputRows.push(0)
        @projectsSelected.push({client:_client, name:_name, component:_component, region:"", hours:_hours})

controllersModule2.controller('CreateWorkLogCtrl', CreateWorkLogCtrl)
