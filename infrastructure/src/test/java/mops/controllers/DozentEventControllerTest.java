package mops.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.c4_soft.springaddons.test.security.context.support.WithIDToken;
import com.c4_soft.springaddons.test.security.context.support.WithMockKeycloackAuth;
import com.tngtech.archunit.thirdparty.com.google.common.base.Charsets;
import mops.Veranstaltung;
import mops.database.MockVeranstaltungsRepository;
import mops.rollen.Dozent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.annotation.PostMapping;

@SpringBootTest
@AutoConfigureMockMvc
@SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
public class DozentEventControllerTest {
  private final transient String orgamail = "orga@mail.de";
  private transient MockVeranstaltungsRepository mockRepo;

  @Autowired
  private transient MockMvc mvc;

  @BeforeEach
  public void init() {
    mockRepo = new MockVeranstaltungsRepository();

    Veranstaltung mockVeranstaltung = new Veranstaltung(
        Long.valueOf(1L), "Test-Veranstaltung",
        "SoSe2020", new Dozent("testDozent"),
        null, null);

    mockRepo.save(mockVeranstaltung);
  }

  @Test
  @DisplayName("Orga sollten auf dozenten/dozent weitergeleitet werden")
  @WithMockKeycloackAuth(roles = "orga", idToken = @WithIDToken(email = orgamail))
  public void redirectOrgaToCorrectPage() throws Exception {
    mvc.perform(get("/feedback/dozenten"))
        .andExpect(status().is2xxSuccessful())
        .andExpect(view().name("dozenten/dozent"));
  }

  @Test
  @DisplayName("VeranstaltungsErstellerseite soll geöffnet werden")
  @WithMockKeycloackAuth(roles = "orga", idToken = @WithIDToken(email = orgamail))
  public void correctRedirectForVeranstaltungsErstellerSeite() throws Exception {
    mvc.perform(get("/feedback/dozenten/event/new"))
        .andExpect(status().is2xxSuccessful())
        .andExpect(view().name("dozenten/neueveranstaltung"));
  }

  @Test
  @DisplayName("Orga sollten auf Veranstaltungsdetails zugreifen können")
  @WithMockKeycloackAuth(roles = "orga", idToken = @WithIDToken(email = orgamail))
  public void correctRedirectForVeranstaltungsDetails() throws Exception {
    mvc.perform(get("/feedback/dozenten/event/1"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(view().name("dozenten/veranstaltung"));
  }

  @Test
  @Disabled
  @DisplayName("Können Veranstaltungen erstellt werden?")
  @WithMockKeycloackAuth(roles = "orga", idToken = @WithIDToken(email = orgamail))
  public void correctRedirectForErstelleNeueVeranstaltung() throws Exception {
    mvc.perform(post("/feedback/dozenten/event/new")
        .requestAttr("veranstaltungsname", "ProPra 2")
        .requestAttr("semester", "SoSe2020"))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/feedback/dozenten"));
  }

  @Test
  @Disabled
  @DisplayName("Können Studenten per Csv-Datei hinzugefügt werden?")
  @WithMockKeycloackAuth(roles = "orga", idToken = @WithIDToken(email = orgamail))
  public void correctRedirectWhenAddingStudents() throws Exception {
    MockMultipartFile file = new MockMultipartFile("file", "students.csv",
        "text/csv", "Kevin,Ben,Clara".getBytes(Charsets.UTF_8));
    mvc.perform(multipart("/feedback/dozenten/event/addStudenten/1")
        .file(file))
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:/feedback/dozenten/event/1"));
  }

  @Test
  @Disabled
  @DisplayName("Können Studenten per Csv-Datei hinzugefügt werden?")
  @WithMockKeycloackAuth(roles = "orga", idToken = @WithIDToken(email = orgamail))
  public void correctRedirectWhenAddingOneStudent() throws Exception {
    mvc.perform(get("/feedback/dozenten/event/addStudent/1"))
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:/feedback/dozenten/event/1"));
  }
}