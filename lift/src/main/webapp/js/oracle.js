var STATE = false;
var root;

function save_state() {
    STATE = $('#jstree').jstree('get_state');
}

function updateButtonState(buttonState) {
    if($.updateButtonState) {
        $.updateButtonState(buttonState)
    }
}

$(function() {
//    try {
        $.ajax({
            url: "../api/graph/center",
            type: "GET",
            data: 'nodeId=0&depth=3',
            success: function(data){
                 root = data
            }
        });
//
        $.jstree.defaults.themes.theme = "../../resources/jstree/themes/apple";

        $("#jstree").jstree({
            'json' : {
                // data function:
                // 'data' : function (o, c) { c.call(this, ['node 1', 'node 2']
                // data static:
                'data' : [
                    {
                        'title' : 'Stories',
                        'data' : { 'jstree' : { 'closed' : true } }
                    },
                    'Root 2',
                    {
                        'title' : 'Oracles',
                        'data' : { 'jstree' : { 'children' : [ 'Child 1', 'Child 2' ] } }
                    },
                    {
                        'title' : 'Clips',
                        'data' : { },
                        'children' : [ 'Child 1', 'Child 2' ]
                    }
                ],
                // mixed with ajax
                'ajax' : {
                    'url' : 'test.json'
                },
                progressive_render : true,
                progressive_unload : true
            },
            "themes" : {
                "theme" : "apple",
                "dots" : false,
                "icons" : false
            },
            'plugins' : [ 'core', 'themes', 'ui', 'json', 'state' ]
        });
        $("#jstree").jstree("set_theme","apple");

        var buttonState = {earth:false, fire:false, water:false, air:false, aether:false, beam:false}

        $(".oracle-button").mouseenter(function() {
            renderButtonState(this.id, true)
        }).mouseleave(function() {
            renderButtonState(this.id, buttonState[this.id])
        }).click(function() {
            $.ajax({
                url: "../api/trigger",
                type: "POST",
                data: 'field=' + this.id
            });
        });

        function renderButtonState(name, state) {
            button = $('#' + name)
            if(button.data == null) {
                button.data = {state:false}
            } else {
                button[0].src = "/images/" + name + (state ? ".png" : "-off.png")
            }
        }

        $.updateButtonState = function(newButtonState) {
            buttonState = newButtonState
            for(key in {earth:false, fire:false, water:false, air:false, aether:false, beam:false}) {
                renderButtonState(key, buttonState[key])
            }
        }

         $("#node-table").jqGrid({
            url: '/api/oracle/nodes',
            datatype: 'json',
            height: 200,
            width: 500,
            colNames:['Id','Script', 'Description'],
            colModel:[
                {name:'id', sortable: true, width:60},
                {name:'script', sortable: true, sorttype: "text", width:90},
                {name:'description', sortable: true, sorttype: "text", width:80}
            ],
            rowNum: -1,
            scroll: true,
            nopager: true,
            multiselect: false,
            sortable: true,
            loadonce: true,
            repeatitems: false,
            jsonReader: {
                root: "rows",
                id: "Id",
                index: "Id",
                page: "page",
                total: "total",
                records: "records",
                repeatitems: false
            },

            //multiselect: true,
            caption: "Oracle Nodes"
        })

        $( "input:submit, a, button", ".demo" ).button();
        $( "a", ".demo" ).click(function() { return false; });
//    } catch(err) {}
})

setTimeout(function() {

    var vis = new pv.Panel()
        .width(200)
        .height(200)
        .left(40)
        .right(160)
        .top(10)
        .bottom(10);

    var layout = vis.add(pv.Layout.Cluster)
        .nodes(pv.dom(root))
        .root("root")
        .sort(function(a, b) {pv.naturalOrder(a.nodeName, b.nodeName)})
        .nodes()
        .group(true)
        .orient("left");

    layout.link.add(pv.Line)
        .strokeStyle("#ccc")
        .lineWidth(1)
        .antialias(false);

    layout.node.add(pv.Dot).fillStyle(function(n) {n.firstChild ? "#aec7e8" : "#ff7f0e"});

    layout.label.add(pv.Label);

    vis.render();
},0)
