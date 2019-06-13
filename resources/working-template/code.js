document.addEventListener('DOMContentLoaded', function() {

  var cy = window.cy = cytoscape({

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
        // {
        //   "selector": "node[?attr]",
        //   "style": {
        //     "shape": "rectangle",
        //     "background-color": "#aaa",
        //     "text-outline-color": "#aaa",
        //     "width": "16px",
        //     "height": "16px",
        //     "font-size": "6px",
        //     "z-index": "1"
        //   }
        // },
        // {
        //   "selector": "node[?query]",
        //   "style": {
        //     "background-clip": "none",
        //     "background-fit": "contain"
        //   }
        // },
        // {
        //   "selector": "node:selected",
        //   "style": {
        //     "border-width": "6px",
        //     "border-color": "#AAD8FF",
        //     "border-opacity": "0.5",
        //     "background-color": "#77828C",
        //     "text-outline-color": "#77828C"
        //   }
        // },
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
        // {
        //   selector: 'edge',
        //   style: {
        //     'width': 3,
        //     'line-color': '#bbb',
        //     'target-arrow-color': '#666',
        //     'target-arrow-shape': 'triangle'
        //   }
        // },
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
        // {
        //   "selector": "edge[group = \"coexp\"]",
        //   "style": {
        //     "line-color": "#d0b7d5"
        //   }
        // },
        // {
        //   "selector": "edge[group = \"coloc\"]",
        //   "style": {
        //     "line-color": "#a0b3dc"
        //   }
        // },
        // {
        //   "selector": "edge[group = \"gi\"]",
        //   "style": {
        //     "line-color": "#90e190"
        //   }
        // },
        // {
        //   "selector": "edge[group = \"path\"]",
        //   "style": {
        //     "line-color": "#9bd8de"
        //   }
        // },
        // {
        //   "selector": "edge[group = \"pi\"]",
        //   "style": {
        //     "line-color": "#eaa2a2"
        //   }
        // },
        // {
        //   "selector": "edge[group = \"predict\"]",
        //   "style": {
        //     "line-color": "#f6c384"
        //   }
        // },
        // {
        //   "selector": "edge[group = \"spd\"]",
        //   "style": {
        //     "line-color": "#dad4a2"
        //   }
        // },
        // {
        //   "selector": "edge[group = \"spd_attr\"]",
        //   "style": {
        //     "line-color": "#D0D0D0"
        //   }
        // },
        // {
        //   "selector": "edge[group = \"reg\"]",
        //   "style": {
        //     "line-color": "#D0D0D0"
        //   }
        // },
        // {
        //   "selector": "edge[group = \"reg_attr\"]",
        //   "style": {
        //     "line-color": "#D0D0D0"
        //   }
        // },
        // {
        //   "selector": "edge[group = \"user\"]",
        //   "style": {
        //     "line-color": "#f0ec86"
        //   }
        // },
        // {
        //   "selector": "node[?social]",
        //   "style": {
        //     "width": 30,
        //     "height": 30,
        //     "background-image": "data(image)",
        //     "background-fit": "cover"
        //   }
        // },
        // {
        //   "selector": "node[?social]:selected",
        //   "style": {
        //     "border-width": 4,
        //     "border-opacity": 0.8
        //   }
        // },
        // {
        //   "selector": "edge[?social]",
        //   "style": {
        //     "width": 3,
        //     "line-color": "#888",
        //     "opacity": 0.4,
        //     "haystack-radius": 0
        //   }
        // },
        {
          "selector": "edge.highlighted",
          "style": {
            "line-color": "#AAD8FF",
            "width": "10px"
          }
        }
      // {
      //   selector: 'node',
      //   style: {
      //     'background-color': '#666',
      //     'label': 'data(id)'
      //   }
      // },


    ],

    layout: {
      name: 'grid',
      // rows: 10
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
  wheelSensitivity: .4,
  pixelRatio: 'auto'

  });

});

