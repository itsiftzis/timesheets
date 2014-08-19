class CreateProjectCtrl

    constructor: (@$log, @$location,  @ProjectService) ->
        @$log.debug "constructing CreateProjectController"
        @projectclient = {}
        @projectname = {}
        @projectcomponent = {}
        @prclients = {}
        @prnames = {}
        @prccurrentclient = {}
        @prcurrentname = {}
        @fetchProjectClients()

    fetchProjectClients: () ->
        @$log.debug "fetchProjectClients()"
        @ProjectService.listProjectClients()
        .then(
          (data) =>
            @$log.debug "Promise returned #{data} Project"
            @prclients = data
        ,
        (error) =>
          @$log.error "Unable to fetch Project Clients: #{error}"
        )

    createProjectClient: () ->
        @$log.debug "createProjectClient()"
        @$log.debug @projectclient
        @ProjectService.createProjectClient(@projectclient)
        .then(
            (data) =>
                @$log.debug "Promise returned #{data} Project"
                @projectclient = data
                @$location.path("/")
            ,
            (error) =>
                @$log.error "Unable to create Project Client: #{error}"
            )

    createProjectName: () ->
        @$log.debug "createProjectName()"
        @projectname.client = @prccurrentclient.client
        @$log.debug @projectname
        @ProjectService.createProjectName(@projectname)
        .then(
            (data) =>
              @$log.debug "Promise returned #{data} Project"
              @projectname = data
              @$location.path("/")
            ,
            (error) =>
              @$log.error "Unable to create Project Name: #{error}"
            )

    createProjectComponent: () ->
        @$log.debug "createProjectComponent()"
        @projectcomponent.client = @prccurrentclient.client
        @projectcomponent.name = @prcurrentname.name
        @ProjectService.createProjectComponent(@projectcomponent)
        .then(
            (data) =>
              @$log.debug "Promise returned #{data} Project"
              @projectcomponent = data
              @$location.path("/")
            ,
            (error) =>
              @$log.error "Unable to create Project Component: #{error}"
            )

    fillNames: (@name) ->
        @$log.debug @name
        @ProjectService.fetchNamesForClient(@name)
        .then(
            (data) =>
              @$log.debug "Promise returned #{data} Project"
              @prnames = data
          ,
          (error) =>
            @$log.error "Unable to get Project names: #{error}"
          )

controllersModule4.controller('CreateProjectCtrl', CreateProjectCtrl)