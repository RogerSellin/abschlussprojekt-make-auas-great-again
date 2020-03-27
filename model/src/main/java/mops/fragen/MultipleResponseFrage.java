package mops.fragen;

import java.util.List;
import mops.antworten.Antwort;

public class MultipleResponseFrage extends MultipleChoiceFrage {


  public MultipleResponseFrage(Long id, String fragentext) {

    super(id, fragentext);
  }

  public MultipleResponseFrage(Long id, String fragentext, List<Auswahl> choices,
      List<Antwort> antworten) {
    super(id, fragentext, choices, antworten);
  }

  @Override
  public Frage clone() {
    return new MultipleResponseFrage(getFragentext(), getChoices());
  }

  public MultipleResponseFrage(String fragetext, List<Auswahl> choices) {
    super(fragetext, choices);
  }
}
