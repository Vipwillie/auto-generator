app.controller('GeneratorCtrl', [
    '$scope',
    '$http',
    'Messager',
    '$modal',
    function ($scope, $http, Messager, $modal) {
        function load(keyword) {
            $scope.loading = true;
            $scope.setting = {
                projectLocation: '',
                developer: '',
                tablePrefix: '',
                model: ''
            };
            $http.get("generator/env?keyword="+keyword).success(
                function (result, status, headers, config) {
                    $scope.loading = false;
                    if (result.success) {
                        $scope.tables = result.data.tables;
                        var params = result.data.params;
                        $scope.setting.projectLocation = params.projectLocation;
                        $scope.setting.developer = params.developer;
                        $scope.setting.tablePrefix = params.tablePrefix;
                    } else {
                        $scope.error = result.message;
                    }
                }).error(function (result, status) {
                $scope.loading = false;
                $scope.error = result.message || status;
            });
        }

        $scope.setup = 0;
        $scope.forward = function (setup) {
            $scope.setup = setup;
        }

        load("");

       $scope.search = function(){
          load($scope.keyword);
       }

       $scope.reset = function(){
          load("");
          $scope.keyword = "";
       }

        $scope.onTableClick = function () {
            var entityName = [];
            var tableComment = [];
            var count = 0;
            angular.forEach($scope.tables, function (item) {
                if (item.checked) {
                    entityName.push(item.tableName);
                    tableComment.push(item.tableComment);
                    count++;
                }
            });
            if (count < 1) {
                Messager.alert("操作提示", "至少选中一行！", "warning");
                return false;
            }
            $scope.setting.tableNames = entityName;
            $scope.setting.entityNames = tableComment;
            $scope.forward(3);
        }

        $scope.parseEnityName = function (table) {
            if (!table) {
                return;
            }
            table = table.toLowerCase();
            if ($scope.setting.tablePrefix) {
                table = table.replace($scope.setting.tablePrefix, "");
            }
            var names = table.split("_");
            var entityName = [];
            for (var i = 0; i < names.length; i++) {
                var name = names[i];
                if (name.length > 0) {
                    var upperName = name.substring(0, 1).toUpperCase() + name.substring(1, name.length);
                    entityName.push(upperName);
                }
            }
            return entityName.join("");
        }


        $scope.generate = function () {

            Messager.confirm("提示", "确定要生成代码?").then(function () {
                var setting = $scope.setting;
                var params = {
                    projectLocation: setting.projectLocation,
                    developer: setting.developer,
                    module: setting.module,
                    tablePrefix: setting.tablePrefix,
                    packageName: setting.packageName,
                    onlyGenerateEntity: setting.onlyGenerateEntity
                };
                for (var i = 0; i < setting.entityNames.length; i++) {
                    params['entityNames[' + i + ']'] = setting.entityNames[i];
                    params['tableNames[' + i + ']'] = setting.tableNames[i];
                }
                $scope.generating = true;
                var toast = $scope.toaster.wait("正在生成...");
                $http.post('generator/generate', params).success(
                    function (data, status, headers, config) {
                        $scope.generating = false;
                        if (data.success) {
                            /* $scope.settingForm.$setPristine();
                             $scope.settingForm.$setUntouched();*/
                            toast.doSuccess("生成成功");
                            $scope.forward(0);
                        } else {
                            toast.doError("生成失败：" + (result.msg || status));
                        }
                    }).error(function (result, status) {
                    $scope.generating = false;
                    toast.doError("生成失败：" + (result.msg || status));
                });
            });


        }
        //全选（取消全选
        $scope.changeAll = function () {
            angular.forEach($scope.tables, function (item) {
                item.checked = $scope.selectAll;
            });
            $scope.count = $scope.selectAll ? $scope.tables.length : 0;
            if ($scope.selectAll) {
                $scope.selectData = $scope.tables;
            } else {
                $scope.selectData = [];
            }
        };
        //单击行选中
        $scope.changeCurrents = function (current, $event) {
            if (current.checked == undefined) {
                current.checked = true;
            } else {
                current.checked = !current.checked;
            }
            $scope.changeCurrent(current, $event);
        };
        $scope.count = 0;//已选择数量
        $scope.selectData = [];//已选对象
        //选择单个(取消选择单个)
        $scope.changeCurrent = function (current, $event) {
            //计算已选数量 true加， false减
            $scope.count += current.checked ? 1 : -1;
            //判断是否全选，选数量等于数据长度为true
            $scope.selectAll = $scope.count === $scope.tables.length;
            //统计已选对象
            $scope.selectData = [];
            angular.forEach($scope.tables, function (item) {
                if (item.checked) {
                    $scope.selectData[$scope.selectData.length] = item;
                }
            });
            $event.stopPropagation();//阻止冒泡
        };
    }]);
