CREATE TABLE node (
    id INT PRIMARY KEY,
    path VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    rawName VARCHAR(255) NOT NULL,
    length BIGINT NOT NULL,
    lastModified BIGINT NOT NULL,
    md5 VARCHAR(32),
    sha1 VARCHAR(40),
    crc32 VARCHAR(8),
    branch BOOLEAN DEFAULT false
);