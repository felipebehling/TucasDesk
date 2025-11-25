CREATE TABLE IF NOT EXISTS leader_election (
    id INT AUTO_INCREMENT PRIMARY KEY,
    service_id VARCHAR(255) NOT NULL,
    last_heartbeat TIMESTAMP NOT NULL
);