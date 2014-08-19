class ProjectCtrl

    constructor: (@$log, @ProjectService) ->
        @$log.debug "constructing ProjectController"
        @projectclients = []
        @projectnames = []
        @projectcomponents = []
        @getAllProjectClient()
        @getAllProjectNames()
        @getAllProjectComponents()

    getAllProjectClient: () ->
        @$log.debug "getAllProjectClients()"

        @ProjectService.listProjectClients()
        .then(
            (data) =>
                @$log.debug "Promise returned #{data.length} Project clients"
                @projectclients = data
            ,
            (error) =>
                @$log.error "Unable to get Project clients: #{error}"
            )

    getAllProjectNames: () ->
        @$log.debug "getAllProjectNames()"

        @ProjectService.listProjectNames()
        .then(
          (data) =>
            @$log.debug "Promise returned #{data.length} Project names"
            @projectnames = data
        ,
        (error) =>
          @$log.error "Unable to get Project names: #{error}"
        )

    getAllProjectComponents: () ->
        @$log.debug "getAllProjectComponents()"

        @ProjectService.listProjectComponents()
        .then(
          (data) =>
            @$log.debug "Promise returned #{data.length} Project components"
            @projectcomponents = data
        ,
        (error) =>
          @$log.error "Unable to get Project components: #{error}"
        )


controllersModule4.controller('ProjectCtrl', ProjectCtrl)