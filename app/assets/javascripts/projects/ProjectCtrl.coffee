class ProjectCtrl

    constructor: (@$log, @ProjectService) ->
        @$log.debug "constructing ProjectController"
        projects = []
        @getAllProjects()

    getAllProjects: () ->
        @$log.debug "getAllProjects()"

        @ProjectService.listProjects()
        .then(
            (data) =>
                @$log.debug "Promise returned #{data.length} Projects"
                @projects = data
            ,
            (error) =>
                @$log.error "Unable to get Projects: #{error}"
            )


controllersModule3.controller('ProjectCtrl', ProjectCtrl)