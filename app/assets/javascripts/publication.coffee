$ ->
  $.get "/publications", (publications) ->
    $.each publications, (index, publication) ->
      $("#publications").append $("<li>").text publications.title