$ ->
  $.get "/sources", (sources) ->
    $.each sources, (index, source) ->
      $("#sources").append $("<li>").text sources.name