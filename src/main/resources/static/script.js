// 初始化地图
const map = new BMap.Map("map");
map.centerAndZoom(new BMap.Point(119.909583, 28.465432), 18);
map.enableScrollWheelZoom(true);

let polyline = null;
let marker = null;

// 全局变量
let allNodes = [];

// 加载下拉框数据
fetch("http://localhost:8080/api/nav/nodes")
    .then(res => res.json())
    .then(nodes => {
        allNodes = nodes; 
        const start = document.getElementById("start");
        const end = document.getElementById("end");

        nodes.forEach(n => {
            const o1 = new Option(n.name, n.id);
            const o2 = new Option(n.name, n.id);
            start.add(o1);
            end.add(o2);
        });
        
        saveOriginalOptions(start);
        saveOriginalOptions(end);
        

    });


function saveOriginalOptions(selectElement) {
    selectElement.originalOptions = [];
    for (let i = 0; i < selectElement.options.length; i++) {
        selectElement.originalOptions.push({
            value: selectElement.options[i].value,
            text: selectElement.options[i].text
        });
    }
}

// 实时搜索功能
const startSearchInput = document.getElementById("start-search");
const startSelect = document.getElementById("start");

startSearchInput.addEventListener("input", function() {
    const searchTerm = this.value.toLowerCase();
    filterSelectOptions(startSelect, searchTerm);
});

const endSearchInput = document.getElementById("end-search");
const endSelect = document.getElementById("end");

endSearchInput.addEventListener("input", function() {
    const searchTerm = this.value.toLowerCase();
    filterSelectOptions(endSelect, searchTerm);
});

// 过滤下拉选项
function filterSelectOptions(selectElement, searchTerm) {
    // 获取所有原始选项（从一个隐藏的备份中）
    if (!selectElement.originalOptions) {
        selectElement.originalOptions = [];
        for (let i = 0; i < selectElement.options.length; i++) {
            selectElement.originalOptions.push({
                value: selectElement.options[i].value,
                text: selectElement.options[i].text
            });
        }
    }
    
    // 清空当前选项
    selectElement.innerHTML = '';
    
    // 根据搜索词过滤并添加选项
    const filteredOptions = selectElement.originalOptions.filter(option => 
        option.text.toLowerCase().includes(searchTerm)
    );
    
    if (filteredOptions.length > 0) {
        filteredOptions.forEach(option => {
            const opt = new Option(option.text, option.value);
            selectElement.add(opt);
        });
    } else {
        const opt = new Option('未找到匹配项', '');
        selectElement.add(opt);
    }
}

// 导航
function nav() {
    const s = document.getElementById("start").value;
    const e = document.getElementById("end").value;
    const mode = document.getElementById("mode").value;

    fetch(`http://localhost:8080/api/nav/path?start=${s}&end=${e}&mode=${mode}`)
        .then(res => res.json())
        .then(res => {
            // 显示结果
            document.getElementById("distance").textContent = res.distance.toFixed(1);
            document.getElementById("time").textContent = (res.time / 60).toFixed(1);
            document.getElementById("result").classList.add("show");
            
            // 显示路径详情
            const pathDetails = document.getElementById("path-details");
            if (pathDetails) {
                let pathHtml = '<h4>途径路段及建筑介绍</h4><ul class="path-list">';
                res.path.forEach((node, index) => {
                    pathHtml += `<li class="path-item">
                        <span class="path-index">${index + 1}.</span>
                        <span class="path-name">${node.name}</span>
                        <span class="path-desc">${node.description || '暂无描述'}</span>
                    </li>`;
                });
                pathHtml += '</ul>';
                pathDetails.innerHTML = pathHtml;
            }

            const data = res.path;
            const points = data.map(p => new BMap.Point(p.lng, p.lat));

            if (polyline) map.removeOverlay(polyline);
            if (marker) map.removeOverlay(marker);

            polyline = new BMap.Polyline(points, {
                strokeColor: "#007bff",
                strokeWeight: 6,
                strokeOpacity: 0.8
            });
            map.addOverlay(polyline);

            marker = new BMap.Marker(points[0]);
            map.addOverlay(marker);

            // 设置地图视野
            if (points.length > 0) {
                map.setViewport(points);
            }

            let i = 0;
            const timer = setInterval(() => {
                if (i >= points.length) {
                    clearInterval(timer);
                    return;
                }
                marker.setPosition(points[i]);
                i++;
            }, 800);
        })
        .catch(error => {
            console.error('导航请求失败:', error);
            alert('导航请求失败，请稍后重试');
        });
}

// 格式化时间显示，根据营业状态添加颜色标记
function formatTimeDisplay(timeStr) {
    if (!timeStr || timeStr === '未知') {
        return '<span class="info-value">未知</span>';
    }

    // 检查是否为时间段格式
    const timeRangeMatch = timeStr.match(/(\d{1,2}:\d{2})-(\d{1,2}:\d{2})/);
    if (!timeRangeMatch) {
        return '<span class="info-value">' + timeStr + '</span>';
    }

    const [_, openTime, closeTime] = timeRangeMatch;
    const now = new Date();
    const currentTime = now.getHours() * 60 + now.getMinutes(); // 转换为分钟
    
    // 解析开门和关门时间
    const [openHour, openMin] = openTime.split(':').map(Number);
    const [closeHour, closeMin] = closeTime.split(':').map(Number);
    const openTimeInMinutes = openHour * 60 + openMin;
    const closeTimeInMinutes = closeHour * 60 + closeMin;

    // 计算距离关门时间的分钟数（30分钟内视为即将关闭）
    const minutesToClose = closeTimeInMinutes - currentTime;
    const isClosed = currentTime < openTimeInMinutes || currentTime > closeTimeInMinutes;
    const isClosingSoon = minutesToClose > 0 && minutesToClose <= 30; // 30分钟内关闭视为即将关闭
    const isOpen = !isClosed && !isClosingSoon;

    let statusText = '';
    let statusClass = '';
    let displayTime = openTime + '-' + closeTime;

    if (isClosed) {
        statusText = ' (已关闭)';
        statusClass = 'status-closed';
        displayTime = openTime + '-' + closeTime + statusText;
    } else if (isClosingSoon) {
        statusText = ' (即将关闭)';
        statusClass = 'status-closing';
        displayTime = openTime + '-' + closeTime + statusText;
    } else if (isOpen) {
        statusText = ' (开放中)';
        statusClass = 'status-open';
        displayTime = openTime + '-' + closeTime + statusText;
    }

    return '<span class="info-value ' + statusClass + '">' + displayTime + '</span>';
}

// 在变量声明部分添加
let infoWindow = null;

// 添加地图点击事件监听
map.addEventListener("click", function(e) {
    // 获取点击位置的经纬度
    const point = e.point;

    // 查询该位置的建筑信息
    fetch(`http://localhost:8080/api/location/coordinates?lng=${point.lng}&lat=${point.lat}`)
        .then(res => res.json())
        .then(data => {
            if (data) {
                // 获取当前日期，判断是工作日还是周末
                const now = new Date();
                const dayOfWeek = now.getDay(); // 0是周日，6是周六
                const isWeekend = (dayOfWeek === 0 || dayOfWeek === 6); // 周六日为周末
                
                // 根据是否为周末选择开放时间
                const openTimeLabel = isWeekend ? "假期开门时间" : "平时开门时间";
                const openTimeValue = isWeekend ? data.openTimeHoliday : data.openTimeSchool;
                
                // 创建信息窗口内容，使用函数格式化时间
                const content = `
                    <div class="info-window">
                        <h4>${data.name || '未知地点'}</h4>
                        <div class="info-item">
                            <span class="info-label">描述：</span>
                            <span class="info-value">${data.description || '暂无描述'}</span>
                        </div>
                        <div class="info-item">
                            <span class="info-label">${openTimeLabel}：</span>
                            <span class="info-value">${formatTimeDisplay(openTimeValue || '未知')}</span>
                        </div>
                    </div>
                `;

                // 创建信息窗口
                infoWindow = new BMap.InfoWindow(content, {
                    width: 360,
                    height: 250,
                    enableAutoPan: true,
                    enableMessage: false,
                    borderRadius: 10 // 设置InfoWindow为圆边角
                });

                // 打开信息窗口
                map.openInfoWindow(infoWindow, point);
            } else {
                alert('该位置没有相关建筑信息');
            }
        })
        .catch(error => {
            console.error('获取地点信息失败:', error);
        });
});