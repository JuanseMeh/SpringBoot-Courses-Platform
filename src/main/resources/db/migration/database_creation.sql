CREATE TABLE category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE workspace (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    owner_id BIGINT NOT NULL,
    CONSTRAINT fk_workspace_owner FOREIGN KEY (owner_id) REFERENCES user(id)
);

CREATE TABLE workspace_member (
    workspace_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    PRIMARY KEY(workspace_id, user_id),
    CONSTRAINT fk_workspace_member_workspace FOREIGN KEY (workspace_id) REFERENCES workspace(id),
    CONSTRAINT fk_workspace_member_user FOREIGN KEY (user_id) REFERENCES user(id)
);

CREATE TABLE course (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    category_id BIGINT NOT NULL,
    CONSTRAINT fk_course_category FOREIGN KEY (category_id) REFERENCES category(id)
);

CREATE TABLE enrollment (
    user_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    PRIMARY KEY(user_id, course_id),
    CONSTRAINT fk_enrollment_user FOREIGN KEY (user_id) REFERENCES user(id),
    CONSTRAINT fk_enrollment_course FOREIGN KEY (course_id) REFERENCES course(id)
);
