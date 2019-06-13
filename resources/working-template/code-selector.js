(function(){
  document.addEventListener('DOMContentLoaded', function(){
    // let $$ = selector => Array.from( document.querySelectorAll( selector ) );
    let $ = selector => document.querySelector( selector );

    let tryPromise = fn => Promise.resolve().then( fn );

    let cy;

    let $layout = $('#layout');
    let maxLayoutDuration = 1500;
    let layoutPadding = 60;

    function exportTableToCSV(filename) {
      var csv = [];
      var rows = document.querySelectorAll("table tr");

      for (var i = 0; i < rows.length; i++) {
          var row = [], cols = rows[i].querySelectorAll("td, th");
          for (var j = 0; j < cols.length; j++) 
              row.push(cols[j].innerText);
          csv.push(row.join(","));
      }
      csv = csv.join("\n")

      var csvFile;
      var downloadLink;

      // CSV file
      csvFile = new Blob([csv], {type: "text/csv"});

      // Download link
      downloadLink = document.createElement("a");

      // File name
      downloadLink.download = filename;

      // Create a link to the file
      downloadLink.href = window.URL.createObjectURL(csvFile);

      // Hide download link
      downloadLink.style.display = "none";

      // Add the link to DOM
      document.body.appendChild(downloadLink);

      // Click download link
      downloadLink.click();
      }

    function export_data() {

      if ($('#export').value == 'png') {
        var image = cy.png( { output: 'blob', scale: $('#scale').value } );
        saveAs( image, 'significant_subgraphs.png' );
      } else {
        var json = new Blob([JSON.stringify(cy.json())], {type: "application/json"});
        saveAs( json, 'significant_subgraphs.json' )
      }
   }

    let layouts = {
      grid: {
        name: 'grid',
        condense: false,
        sort: function(a, b){ return a.data('pvalue') - b.data('pvalue')
      },
      animate: true,
      animationDuration: maxLayoutDuration,
      padding: layoutPadding
    },
    circle: {
        name: 'circle',
        animate: true,
        animationDuration: maxLayoutDuration,
        padding: layoutPadding
    },
    cose: {
        name: 'cose',
        animate: 'end',
        // animationDuration: maxLayoutDuration
        padding: layoutPadding,
        gravity: 2,
        nodeOverlap: 4,
        idealEdgeLength: function( edge ){ return 100; },
        componentSpacing: 25
    },
    concentric: {
        name: 'concentric',
        animate: true,
        animationDuration: maxLayoutDuration,
        padding: layoutPadding
    },
    random: {
      name: 'random',
      animate: true,
      animationDuration: maxLayoutDuration,
      padding: layoutPadding,
    }
  };
    let prevLayout;
    let getLayout = name => Promise.resolve( layouts[ name ] );
    let applyLayout = layout => {
      if( prevLayout ){
        prevLayout.stop();
      }

      let l = prevLayout = cy.makeLayout( layout );

      return l.run().promiseOn('layoutstop');
    }
    let applyLayoutFromSelect = () => Promise.resolve( $layout.value ).then( getLayout ).then( applyLayout );
    cy = window.cy = cytoscape({

    //   container: $('#cy')
    container: document.getElementById('cy'), // container to render in

    elements: [{"group":"nodes","data":{"id":"2ILE-1GLU-2","name":"ILE","pvalue":-23.95382200785512},"removed":false,"selected":false,"selectable":true,"locked":false,"grabbable":true},{"group":"nodes","data":{"id":"2ILE-1GLU-1","name":"GLU","pvalue":-23.95382200785512},"removed":false,"selected":false,"selectable":true,"locked":false,"grabbable":true},{"group":"edges","data":{"id":"2ILE-1GLU-2-1","source":"2ILE-1GLU-2","target":"2ILE-1GLU-1","pvalue":-23.95382200785512},"removed":false,"selected":false,"selectable":true,"locked":false,"grabbable":true},{"group":"nodes","data":{"id":"1ASP-2ILE-1","name":"ASP","pvalue":-40.36339182575347},"removed":false,"selected":false,"selectable":true,"locked":false,"grabbable":true},{"group":"nodes","data":{"id":"1ASP-2ILE-2","name":"ILE","pvalue":-40.36339182575347},"removed":false,"selected":false,"selectable":true,"locked":false,"grabbable":true},{"group":"edges","data":{"id":"1ASP-2ILE-1-2","source":"1ASP-2ILE-1","target":"1ASP-2ILE-2","pvalue":-40.36339182575347},"removed":false,"selected":false,"selectable":true,"locked":false,"grabbable":true},{"group":"nodes","data":{"id":"2GLY-1GLU-2","name":"GLY","pvalue":-22.408780304593442},"removed":false,"selected":false,"selectable":true,"locked":false,"grabbable":true},{"group":"nodes","data":{"id":"2GLY-1GLU-1","name":"GLU","pvalue":-22.408780304593442},"removed":false,"selected":false,"selectable":true,"locked":false,"grabbable":true},{"group":"edges","data":{"id":"2GLY-1GLU-2-1","source":"2GLY-1GLU-2","target":"2GLY-1GLU-1","pvalue":-22.408780304593442},"removed":false,"selected":false,"selectable":true,"locked":false,"grabbable":true},{"group":"nodes","data":{"id":"2THR-1ASP-2","name":"THR","pvalue":-28.356063651449936},"removed":false,"selected":false,"selectable":true,"locked":false,"grabbable":true},{"group":"nodes","data":{"id":"2THR-1ASP-1","name":"ASP","pvalue":-28.356063651449936},"removed":false,"selected":false,"selectable":true,"locked":false,"grabbable":true},{"group":"edges","data":{"id":"2THR-1ASP-2-1","source":"2THR-1ASP-2","target":"2THR-1ASP-1","pvalue":-28.356063651449936},"removed":false,"selected":false,"selectable":true,"locked":false,"grabbable":true},{"group":"nodes","data":{"id":"1ASP-2ILE,3THR-2ILE-1","name":"ASP","pvalue":-47.343690173597196},"removed":false,"selected":false,"selectable":true,"locked":false,"grabbable":true},{"group":"nodes","data":{"id":"1ASP-2ILE,3THR-2ILE-2","name":"ILE","pvalue":-47.343690173597196},"removed":false,"selected":false,"selectable":true,"locked":false,"grabbable":true},{"group":"nodes","data":{"id":"1ASP-2ILE,3THR-2ILE-3","name":"THR","pvalue":-47.343690173597196},"removed":false,"selected":false,"selectable":true,"locked":false,"grabbable":true},{"group":"edges","data":{"id":"1ASP-2ILE,3THR-2ILE-1-2","source":"1ASP-2ILE,3THR-2ILE-1","target":"1ASP-2ILE,3THR-2ILE-2","pvalue":-47.343690173597196},"removed":false,"selected":false,"selectable":true,"locked":false,"grabbable":true},{"group":"edges","data":{"id":"1ASP-2ILE,3THR-2ILE-3-2","source":"1ASP-2ILE,3THR-2ILE-3","target":"1ASP-2ILE,3THR-2ILE-2","pvalue":-47.343690173597196},"removed":false,"selected":false,"selectable":true,"locked":false,"grabbable":true},{"group":"nodes","data":{"id":"2GLY-1ASP-2","name":"GLY","pvalue":-23.169066574949415},"removed":false,"selected":false,"selectable":true,"locked":false,"grabbable":true},{"group":"nodes","data":{"id":"2GLY-1ASP-1","name":"ASP","pvalue":-23.169066574949415},"removed":false,"selected":false,"selectable":true,"locked":false,"grabbable":true},{"group":"edges","data":{"id":"2GLY-1ASP-2-1","source":"2GLY-1ASP-2","target":"2GLY-1ASP-1","pvalue":-23.169066574949415},"removed":false,"selected":false,"selectable":true,"locked":false,"grabbable":true},{"group":"nodes","data":{"id":"2VAL-1GLU-2","name":"VAL","pvalue":-16.845819507029763},"removed":false,"selected":false,"selectable":true,"locked":false,"grabbable":true},{"group":"nodes","data":{"id":"2VAL-1GLU-1","name":"GLU","pvalue":-16.845819507029763},"removed":false,"selected":false,"selectable":true,"locked":false,"grabbable":true},{"group":"edges","data":{"id":"2VAL-1GLU-2-1","source":"2VAL-1GLU-2","target":"2VAL-1GLU-1","pvalue":-16.845819507029763},"removed":false,"selected":false,"selectable":true,"locked":false,"grabbable":true},{"group":"nodes","data":{"id":"1GLU-2GLY-1","name":"GLU","pvalue":-17.207664744299002},"removed":false,"selected":false,"selectable":true,"locked":false,"grabbable":true},{"group":"nodes","data":{"id":"1GLU-2GLY-2","name":"GLY","pvalue":-17.207664744299002},"removed":false,"selected":false,"selectable":true,"locked":false,"grabbable":true},{"group":"edges","data":{"id":"1GLU-2GLY-1-2","source":"1GLU-2GLY-1","target":"1GLU-2GLY-2","pvalue":-17.207664744299002},"removed":false,"selected":false,"selectable":true,"locked":false,"grabbable":true},{"group":"nodes","data":{"id":"2ASP-1GLU-2","name":"ASP","pvalue":-25.25938499302274},"removed":false,"selected":false,"selectable":true,"locked":false,"grabbable":true},{"group":"nodes","data":{"id":"2ASP-1GLU-1","name":"GLU","pvalue":-25.25938499302274},"removed":false,"selected":false,"selectable":true,"locked":false,"grabbable":true},{"group":"edges","data":{"id":"2ASP-1GLU-2-1","source":"2ASP-1GLU-2","target":"2ASP-1GLU-1","pvalue":-25.25938499302274},"removed":false,"selected":false,"selectable":true,"locked":false,"grabbable":true},{"group":"nodes","data":{"id":"2ALA-1ASP-2","name":"ALA","pvalue":-27.155891918689203},"removed":false,"selected":false,"selectable":true,"locked":false,"grabbable":true},{"group":"nodes","data":{"id":"2ALA-1ASP-1","name":"ASP","pvalue":-27.155891918689203},"removed":false,"selected":false,"selectable":true,"locked":false,"grabbable":true},{"group":"edges","data":{"id":"2ALA-1ASP-2-1","source":"2ALA-1ASP-2","target":"2ALA-1ASP-1","pvalue":-27.155891918689203},"removed":false,"selected":false,"selectable":true,"locked":false,"grabbable":true},{"group":"nodes","data":{"id":"1ASP-2VAL-1","name":"ASP","pvalue":-22.8597203148918},"removed":false,"selected":false,"selectable":true,"locked":false,"grabbable":true},{"group":"nodes","data":{"id":"1ASP-2VAL-2","name":"VAL","pvalue":-22.8597203148918},"removed":false,"selected":false,"selectable":true,"locked":false,"grabbable":true},{"group":"edges","data":{"id":"1ASP-2VAL-1-2","source":"1ASP-2VAL-1","target":"1ASP-2VAL-2","pvalue":-22.8597203148918},"removed":false,"selected":false,"selectable":true,"locked":false,"grabbable":true},{"group":"nodes","data":{"id":"2THR-1GLU-2","name":"THR","pvalue":-28.974013837522822},"removed":false,"selected":false,"selectable":true,"locked":false,"grabbable":true},{"group":"nodes","data":{"id":"2THR-1GLU-1","name":"GLU","pvalue":-28.974013837522822},"removed":false,"selected":false,"selectable":true,"locked":false,"grabbable":true},{"group":"edges","data":{"id":"2THR-1GLU-2-1","source":"2THR-1GLU-2","target":"2THR-1GLU-1","pvalue":-28.974013837522822},"removed":false,"selected":false,"selectable":true,"locked":false,"grabbable":true},{ data: { id: 'a' } },{ data: { id: 'b' } },{data: {id: 'ab',source: 'a',target: 'b'}}],
    style: [ // the stylesheet for the graph
        { // global UI properties
          "selector": "core",
          "style": {
            "selection-box-color": "#7ddb7d", //"#AAD8FF",
            "selection-box-border-color": "#008000", //"#8BB0D0",
            "selection-box-opacity": "0.5"
          }
        },
        {
          "selector": "node",
          "style": {
            "width": "mapData(pvalue, -50, 0, 80, 20)", // log p value range
            "height": "mapData(pvalue, -50, 0, 80, 20)",
            "label": "data(name)",
            "font-size": "mapData(pvalue, -50, 0, 25px, 14px)",
            "text-valign": "center",
            "text-halign": "center",
            "background-color": "mapData(pvalue, -50, 0, #555, #ccc)",
            "border-width": "1px",
            "border-color": "#3c3c3c", // 555
            // "border-opacity": "0.5",
            "text-outline-color": "#3c3c3c", // 555
            "text-outline-width": "2px",
            "color": "#fff",  // label colour
            "overlay-padding": "6px",
            "z-index": "10"
          },
        },
        {
          "selector": "node:selected",
          "style": {
            "border-width": "6px",
            "border-color": "#AAD8FF",//"#008000",
            "border-opacity": "0.4",
            "background-color": "#77828C", //"#7e7f7b",
            // "background-opacity": "0.3",
            "text-outline-color": "#77828C" //"#7e7f7b"
          }
        },
        {
          "selector": "edge",
          "style": {
            "curve-style": "unbundled-bezier", // haystack
            "haystack-radius": "0",
            "opacity": "0.4",
            "line-color": "#bbb",
            "width": 2,
            "width": "mapData(pvalue, 0, -50, 1, 7)",
            "overlay-padding": "3px",
            "target-arrow-shape": "triangle-backcurve",
            "target-arrow-fill": "fill",
            'target-arrow-color': '#bbb'
          }
        },
        {
          "selector": "edge:selected",
          "style": {
            "width": 10,
            'line-color': "#8BB0D0",// '#008000',
            'target-arrow-color': "#8BB0D0"// '#008000'
          }
        },
        {
          "selector": "node.unhighlighted",
          "style": {
            "opacity": "1"
          }
        },
        {
          "selector": "edge.unhighlighted",
          "style": {
            "opacity": "0.05"
          }
        },
        {
          "selector": ".highlighted",
          "style": {
            "z-index": "999999",
            "transition-duration": "0.5s"
          }
        },
        {
          "selector": "node.highlighted",
          "style": {
            "border-width": "8px",
            "border-color": "#AAD8FF",
            "border-opacity": "0.85",
            "background-color": "#394855",
            "text-outline-color": "#394855",
            "shadow-blur": "12px",
            "shadow-color": "#000",
            "shadow-opacity": "0.8",
            "shadow-offset-x": "0px",
            "shadow-offset-y": "4px",
            "transition-property": "border-width, border-color, border-opacity, background-color, text-outline-color, shadow-blur, shadow-color, shadow-opacity, shadow-offset-x, shadow-offset-y"
          }
        },
        {
          "selector": "node.start",
          "style": {
            "border-color": "#8FDC97"
          }
        },
        {
          "selector": "node.end",
          "style": {
            "border-color": "#9F4A54"
          }
        },
        {
          "selector": "edge.filtered",
          "style": {
            "opacity": "0"
          }
        },

        {
          "selector": "edge.highlighted",
          "style": {
            "line-color": "#AAD8FF",
            "width": "10px"
          }
        }
    ],

    layout: {
      name: 'grid',
      condense: false,
      sort: function(a, b){ return a.data('pvalue') - b.data('pvalue') }
    },

  // initial viewport state:
  zoom: 1,
  pan: { x: 0, y: 0 },

  // interaction options:
  minZoom: 1e-1,
  maxZoom: 1e1,
  zoomingEnabled: true,
  userZoomingEnabled: true,
  panningEnabled: true,
  userPanningEnabled: true,
  boxSelectionEnabled: true,
  selectionType: 'single',
  touchTapThreshold: 8,
  desktopTapThreshold: 4,
  autolock: false,
  autoungrabify: false,
  autounselectify: false,

  // rendering options:
  headless: false,
  styleEnabled: true,
  hideEdgesOnViewport: false,
  hideLabelsOnViewport: false,
  textureOnViewport: false,
  motionBlur: false,
  motionBlurOpacity: 0.2,
  wheelSensitivity: .1,
  pixelRatio: 'auto'

  });

    tryPromise( applyLayoutFromSelect );

    $layout.addEventListener('change', applyLayoutFromSelect);

    $('#redo-layout').addEventListener('click', applyLayoutFromSelect);

    $('#save_button').addEventListener('click', export_data);

    $('#save_table_button').addEventListener('click', exportTableToCSV.bind(null, 'significant_subgraphs.csv'));

  });

})();
