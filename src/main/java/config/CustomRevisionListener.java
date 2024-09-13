package config;

import org.hibernate.envers.RevisionListener;
import audit.Revision;

public class CustomRevisionListener implements RevisionListener {
    public void newRevision(Object entidad){
        final Revision revision = (Revision)entidad;
    }
}
