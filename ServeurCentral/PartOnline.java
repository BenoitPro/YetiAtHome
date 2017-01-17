
public class PartOnline {
    private ThreadTraitementParticipant   Tp;
    private String              adresse;
    
    public PartOnline( String adresse, ThreadTraitementParticipant Tp) {
        this.Tp = Tp;
        this.adresse = adresse;
    }
    
    public String getAdresse() { return this.adresse; }
    
    public ThreadTraitementParticipant getThread() { return this.Tp; }
}
