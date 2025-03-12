package edu.stanford.protege.webprotege.access;

import edu.stanford.protege.webprotege.authorization.BasicCapability;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@AutoConfigureJsonTesters
public class RoleAssignmentDeserializationTestCase {

    @Autowired
    private JacksonTester<RoleAssignment> tester;

    @Test
    public void shouldDeserializeRoleAssignmentFromJson() throws Exception {
        var json = """
                {
                	"userName": "matthewhorridge",
                	"projectId": "2f8bd662-512b-4224-a1ac-67e5bd142094",
                	"assignedRoles": [
                		"ProjectDownloader",
                		"CanManage"
                	],
                	"roleClosure": [
                		"ProjectDownloader",
                		"LayoutEditor",
                		"IssueCommenter",
                		"CanView",
                		"IssueViewer",
                		"ProjectDownloader",
                		"CanEdit",
                		"CanManage",
                		"IssueManager",
                		"CanComment",
                		"ProjectManager",
                		"ObjectCommenter",
                		"ProjectViewer",
                		"ProjectEditor",
                		"IssueCreator"
                	],
                	"capabilityClosure": [
                		{
                			"@type": "BasicCapability",
                			"id": "CreateClass"
                		},
                		{
                			"@type": "BasicCapability",
                			"id": "DeleteClass"
                		}
                	]
                }
                """;
            var parsed = tester.parse(json);
            var roleAssignment = parsed.getObject();
            assertThat(roleAssignment.getCapabilityClosure())
                    .containsExactlyInAnyOrder(
                            BasicCapability.valueOf("CreateClass"),
                            BasicCapability.valueOf("DeleteClass")
                    );
    }

    @Test
    public void shouldDeserializeRoleAssignmentFromLegacyJson() throws Exception {
        var json = """
                {
                  "userName": "matthewhorridge",
                  "projectId": "2f8bd662-512b-4224-a1ac-67e5bd142094",
                  "assignedRoles": [
                    "ProjectDownloader",
                    "CanManage"
                  ],
                  "roleClosure": [
                    "ProjectDownloader",
                    "LayoutEditor",
                    "IssueCommenter",
                    "CanView",
                    "IssueViewer",
                    "ProjectDownloader",
                    "CanEdit",
                    "CanManage",
                    "IssueManager",
                    "CanComment",
                    "ProjectManager",
                    "ObjectCommenter",
                    "ProjectViewer",
                    "ProjectEditor",
                    "IssueCreator"
                  ],
                  "actionClosure": [
                    "CreateClass",
                    "DeleteClass"
                  ]
                }
                """;
        var parsed = tester.parse(json);
        var roleAssignment = parsed.getObject();
        assertThat(roleAssignment.getCapabilityClosure())
                .containsExactlyInAnyOrder(
                        BasicCapability.valueOf("CreateClass"),
                        BasicCapability.valueOf("DeleteClass")
                );
    }
}
