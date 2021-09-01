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
      lemp.symbols[i].firstset = Set.new_();
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
            progress += Set.add(s1.firstset, s2.index);
            break;
          } else if (s1 == s2) {
            if (!s1.lambda) break;
          } else {
            progress += Set.union(s1.firstset, s2.firstset);
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
      Set.add(newcfp.fws, 0);
    }
    getstate(lemp);
  }

  public static void getstate(Lemon lemp) {
    //TODO

    //  struct config *cfp, *bp;
    //  struct state *stp;
    //
    //  /* Extract the sorted basis of the new state.  The basis was constructed
    //  ** by prior calls to "Configlist_addbasis()". */
    //  Configlist_sortbasis();
    //  bp = Configlist_basis();
    //
    //  /* Get a state with the same basis */
    //  stp = State_find(bp);
    //  if( stp ){
    //    /* A state with the same basis already exists!  Copy all the follow-set
    //    ** propagation links from the state under construction into the
    //    ** preexisting state, then return a pointer to the preexisting state */
    //    struct config *x, *y;
    //    for(x=bp, y=stp->bp; x && y; x=x->bp, y=y->bp){
    //      Plink_copy(&y->bplp,x->bplp);
    //      Plink_delete(x->fplp);
    //      x->fplp = x->bplp = 0;
    //    }
    //    cfp = Configlist_return();
    //    Configlist_eat(cfp);
    //  }else{
    //    /* This really is a new state.  Construct all the details */
    //    Configlist_closure(lemp);    /* Compute the configuration closure */
    //    Configlist_sort();           /* Sort the configuration closure */
    //    cfp = Configlist_return();   /* Get a pointer to the config list */
    //    stp = State_new();           /* A new state structure */
    //    MemoryCheck(stp);
    //    stp->bp = bp;                /* Remember the configuration basis */
    //    stp->cfp = cfp;              /* Remember the configuration closure */
    //    stp->index = lemp->nstate++; /* Every state gets a sequence number */
    //    stp->ap = 0;                 /* No actions, yet. */
    //    State_insert(stp,stp->bp);   /* Add to the state table */
    //    buildshifts(lemp,stp);       /* Recursively compute successor states */
    //  }
    //  return stp;
  }
}
