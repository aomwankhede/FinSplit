package aom.finsplit.finsplit.entities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.springframework.stereotype.Component;

class Pair {
    public long id;
    public double amount;

    public Pair(long id, double amount) {
        this.id = id;
        this.amount = amount;
    }
}

@Component
public class DebtGraph {
    private final Map<Long, Map<Long, Double>> graph = new HashMap<>();
    ReadWriteLock lock = new ReentrantReadWriteLock(true);

    // Add or update debt: u owes v
    public boolean addDebt(long fromUserId, long toUserId, double amount) {
        if (fromUserId == toUserId || amount <= 0)
            return false;
        lock.writeLock().lock();
        try {
            if (graph.containsKey(toUserId) && graph.get(toUserId).containsKey(fromUserId)) {
                double newWeight = amount - graph.get(toUserId).get(fromUserId);
                if (newWeight < 0) {
                    graph.get(toUserId).put(fromUserId, -1 * newWeight);
                } else if (newWeight > 0) {
                    graph.get(toUserId).remove(fromUserId);
                    graph.putIfAbsent(fromUserId, new HashMap<>());
                    graph.get(fromUserId).put(toUserId, newWeight);
                } else {
                    graph.get(toUserId).remove(fromUserId);
                }
                if (graph.get(toUserId).isEmpty()) {
                    graph.remove(toUserId);
                }
                return true;
            }
            graph.putIfAbsent(fromUserId, new HashMap<>());
            graph.get(fromUserId).putIfAbsent(toUserId, 0.0);
            graph.get(fromUserId).put(toUserId, amount + graph.get(fromUserId).get(toUserId));
            return true;
        } catch (Exception e) {
            System.out.println("Some error occured" + e.getMessage());
            return false;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Map<Long, Map<Long, Double>> getAllDebts() {
        lock.readLock().lock();
        try {
            Map<Long, Map<Long, Double>> clone = new HashMap<>();
            for (Map.Entry<Long, Map<Long, Double>> entry : graph.entrySet()) {
                clone.put(entry.getKey(), new HashMap<>(entry.getValue()));
            }
            return clone;
        } finally {
            lock.readLock().unlock();
        }
    }

    public Map<Long, Double> getDebtsByUser(Long userId) {
        lock.readLock().lock();
        try {
            return graph.containsKey(userId)
                    ? new HashMap<>(graph.get(userId))
                    : new HashMap<>();
        } finally {
            lock.readLock().unlock();
        }
    }

    public List<Pair> getOwesById(long id) {
        lock.readLock().lock();
        try {
            List<Pair> ls = graph.keySet().stream()
                    .filter(userId -> graph.containsKey(userId) && graph.get(userId).containsKey(id))
                    .map(userId -> new Pair(userId, graph.get(userId).get(id)))
                    .toList();
            return ls;
        } finally {
            lock.readLock().unlock();
        }
    }

    public boolean settleDebt(long toUserId, long fromUserId, double amount) {
        if (fromUserId == toUserId || amount <= 0) {
            return false;
        }
        try {
            lock.writeLock().lock();
            if (!graph.containsKey(fromUserId) || !graph.get(fromUserId).containsKey(toUserId)) {
                System.out.println("No debt to pay!");
            } else {
                System.out.println("Hello1");
                double weight = graph.get(fromUserId).get(toUserId);
                if (amount == weight) {
                    System.out.println("Hello2");
                    graph.get(fromUserId).remove(toUserId);
                    if (graph.get(fromUserId).size() == 0) {
                        System.out.println("Hello3");
                        graph.remove(fromUserId);
                    }
                } else if (amount < weight) {
                    System.out.println("HelloImp");
                    graph.get(fromUserId).put(toUserId, weight - amount);
                } else {
                    System.out.println("PingPong");
                    graph.get(fromUserId).remove(toUserId);
                    addDebt(toUserId, fromUserId, amount - weight);
                }
            }
            return true;
        } catch (Exception e) {
            System.out.println("Error Occured :" + e.getMessage());
            return false;
        } finally {
            lock.writeLock().unlock();
        }
    }

}
