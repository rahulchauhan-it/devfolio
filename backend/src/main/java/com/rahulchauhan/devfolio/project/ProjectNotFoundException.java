package com.rahulchauhan.devfolio.project;

public class ProjectNotFoundException extends RuntimeException {

    public ProjectNotFoundException(String slug) {
        super("Project not found: " + slug);
    }
}
