package com.example.campus.service;


import com.example.campus.entity.NavEdge;
import com.example.campus.entity.NavNode;
import com.example.campus.mapper.NavEdgeMapper;
import com.example.campus.mapper.NavNodeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class  NavService {

    @Autowired
    private NavNodeMapper nodeMapper;
    @Autowired
    private NavEdgeMapper edgeMapper;

    // Dijkstra 最短路径
    public List<NavNode> findPath(Long startId, Long endId) {

        // 1. 查询所有节点和边
        List<NavNode> nodes = nodeMapper.selectList(null);
        List<NavEdge> edges = edgeMapper.selectList(null);
        // 2. 构建邻接表
        Map<Long, List<NavEdge>> graph = new HashMap<>();
        for (NavEdge e : edges) {
            graph.computeIfAbsent(e.getFromNode(), k -> new ArrayList<>()).add(e);
        }
        // 3. Dijkstra 初始化
        Map<Long, Double> dist = new HashMap<>();
        Map<Long, Long> prev = new HashMap<>();
        Set<Long> visited = new HashSet<>();
        for (NavNode n : nodes) {
            dist.put(n.getId(), Double.MAX_VALUE);
        }
        dist.put(startId, 0.0);
        // 4. 主循环
        while (true) {
            Long cur = null;
            double min = Double.MAX_VALUE;

            for (Long id : dist.keySet()) {
                if (!visited.contains(id) && dist.get(id) < min) {
                    min = dist.get(id);
                    cur = id;
                }
            }
            if (cur == null || cur.equals(endId)) {
                break;
            }
            visited.add(cur);
            List<NavEdge> list = graph.getOrDefault(cur, Collections.emptyList());
            for (NavEdge e : list) {
                double nd = dist.get(cur) + e.getDistance();
                Double existingDist = dist.get(e.getToNode());
                if (existingDist == null || nd < existingDist) {
                    dist.put(e.getToNode(), nd);
                    prev.put(e.getToNode(), cur);
                }
            }
        }
        // 5. 回溯路径
        LinkedList<NavNode> path = new LinkedList<>();
        Long current = endId;
        while (current != null) {
            NavNode node = nodeMapper.selectById(current);
            path.addFirst(node);
            current = prev.get(current);
        }
        return path;
    }

    // 计算路径总距离（米）
    public double calcDistance(List<NavNode> path) {
        double sum = 0;

        for (int i = 0; i < path.size() - 1; i++) {
            sum += haversine(
                    path.get(i).getLat(),
                    path.get(i).getLng(),
                    path.get(i + 1).getLat(),
                    path.get(i + 1).getLng()
            );
        }
        return sum;
    }
    // Haversine 公式（地球两点距离）
    private double haversine(double lat1, double lng1,
                             double lat2, double lng2) {
        final double R = 6371000; // 地球半径（米）
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLng / 2)
                * Math.sin(dLng / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
