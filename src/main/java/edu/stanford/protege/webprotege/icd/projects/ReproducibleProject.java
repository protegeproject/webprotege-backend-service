package edu.stanford.protege.webprotege.icd.projects;

public record ReproducibleProject(String projectId, long lastBackupTimestamp, String associatedBranch) {
}
