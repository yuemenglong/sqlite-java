package io.github.yuemenglong.sqlite.lemon;

public class Build {
    // FindRulePrecedences
    public static void findRulePrecedences(Lemon xp) {
        Rule rp;
        for (rp = xp.rule; rp != null; rp = rp.next) {
            if (rp.precsym == null) {
                for (int i = 0; i < rp.nrhs; i++) {
                    if (rp.rhs[i].prec >= 0) {
                        rp.precsym = rp.rhs[i];
                        break;
                    }
                }
            }
        }
    }

    // FindFirstSets
    public static void findFirstSets(Lemon lemp) {
        int i;
        Rule rp;
        int progress;
        for (i = 0; i < lemp.nsymbol; i++) {
            lemp.symbols[i].lambda = false;
        }
        for (i = lemp.nterminal; i < lemp.nsymbol; i++) {
            lemp.symbols[i].firstset = Set.setNew();
        }
        do {
            progress = 0;
            for (rp = lemp.rule; rp != null; rp = rp.next) {
                if (rp.lhs.lambda) continue;
                for (i = 0; i < rp.nrhs; i++) {
                    if (!rp.rhs[i].lambda) break;
                }
                if (i == rp.nrhs) {
                    rp.lhs.lambda = true;
                    progress = 1;
                }
            }
        } while (progress != 0);
        do {
            Symbol s1, s2;
            progress = 0;
            for (rp = lemp.rule; rp != null; rp = rp.next) {
                s1 = rp.lhs;
                for (i = 0; i < rp.nrhs; i++) {
                    s2 = rp.rhs[i];
                    if (s2.type == Symbol.SymbolType.TERMINAL) {
                        progress += Set.setAdd(s1.firstset, s2.index);
                        break;
                    } else if (s1 == s2) {
                        if (!s1.lambda) break;
                    } else {
                        progress += Set.setUnion(s1.firstset, s2.firstset);
                        if (!s2.lambda) break;
                    }
                }
            }
        } while (progress != 0);
    }
    //
    //  /* Now compute all first sets */
    //  do{
    //    struct symbol *s1, *s2;
    //    progress = 0;
    //    for(rp=lemp->rule; rp; rp=rp->next){
    //      s1 = rp->lhs;
    //      for(i=0; i<rp->nrhs; i++){
    //        s2 = rp->rhs[i];
    //        if( s2->type==TERMINAL ){
    //          progress += SetAdd(s1->firstset,s2->index);
    //          break;
    //	}else if( s1==s2 ){
    //          if( s1->lambda==FALSE ) break;
    //	}else{
    //          progress += SetUnion(s1->firstset,s2->firstset);
    //          if( s2->lambda==FALSE ) break;
    //	}
    //      }
    //    }
    //  }while( progress );
    //  return;
    //}
}
