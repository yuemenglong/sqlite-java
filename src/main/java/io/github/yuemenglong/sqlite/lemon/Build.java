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

    // FindStates
    public static void findState(Lemon lemp) {
        Symbol sp;
        Rule rp;
        Config.init();
        if (lemp.start != null) {
            sp = Symbol.find(lemp.start);
            if (sp == null) {
                Error.msg(lemp.filename, 0,
                        "The specified start symbol \"%s\" is not " +
                                " in a nonterminal of the grammar.  \"%s\" will be used as the start " +
                                " symbol instead.", lemp.start, lemp.rule.lhs.name);
                lemp.errorcnt += 1;
                sp = lemp.rule.lhs;
            }
        } else {
            sp = lemp.rule.lhs;
        }
        for (rp = lemp.rule; rp != null; rp = rp.next) {
            int i;
            for (i = 0; i < rp.nrhs; i++) {
                if (rp.rhs[i] == sp) {
                    Error.msg(lemp.filename, 0,
                            "The start symbol \"%s\" occurs on the" +
                                    "right-hand side of a rule. This will result in a parser which " +
                                    "does not work properly.", sp.name);
                    lemp.errorcnt++;
                }
            }
        }
        for (rp = sp.rule; rp != null; rp = rp.nextlhs) {
            Config newcfp;
            newcfp = Configlist.addbasis(rp, 0);
            Set.setAdd(newcfp.fws, 0);
        }
        //  for(rp=sp->rule; rp; rp=rp->nextlhs){
        //    struct config *newcfp;
        //    newcfp = Configlist_addbasis(rp,0);
        //    SetAdd(newcfp->fws,0);
        //  }
        //
        //  /* Compute the first state.  All other states will be
        //  ** computed automatically during the computation of the first one.
        //  ** The returned pointer to the first state is not used. */
        //  (void)getstate(lemp);
        //  return;
    }
}
