package io.github.yuemenglong.sqlite.lemon;

import io.github.yuemenglong.sqlite.common.Addr;
import io.github.yuemenglong.sqlite.lemon.util.Assert;

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
  public static void findStates(Lemon lemp) {
    Symbol sp;
    Rule rp;
    Configlist.init();
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

  public static State getstate(Lemon lemp) {
    Config cfp, bp;
    State stp;
    Configlist.sortbasis();
    bp = Configlist.basis();
    stp = State.find(bp);
    if (stp != null) {
      Config x, y;
      for (x = bp, y = stp.bp; x != null && y != null; x = x.bp, y = y.bp) {
        Config yy = y;
        Plink.copy(new Addr<>(() -> yy.bplp, v -> yy.bplp = v), x.bplp);
        Plink.delete(x.fplp);
        x.fplp = null;
        x.bplp = null;
      }
      cfp = Configlist.return_();
      Configlist.eat(cfp);
    } else {
      Configlist.closure(lemp);
      Configlist.sort();
      cfp = Configlist.return_();
      stp = State.new_();
      stp.bp = bp;
      stp.cfp = cfp;
      stp.index = lemp.nstate++;
      stp.ap = null;
      State.insert(stp, stp.bp);
      buildshifts(lemp, stp);
    }
    return stp;
  }

  public static void buildshifts(Lemon lemp, State stp) {
    Config cfp;
    Config bcfp;
    Config new_;
    Symbol sp;
    Symbol bsp;
    State newstp;
    for (cfp = stp.cfp; cfp != null; cfp = cfp.next) cfp.status = Config.Status.INCOMPLETE;

    for (cfp = stp.cfp; cfp != null; cfp = cfp.next) {
      if (cfp.status == Config.Status.COMPLETE) continue;
      if (cfp.dot >= cfp.rp.nrhs) continue;
      Configlist.reset();
      sp = cfp.rp.rhs[cfp.dot];

      for (bcfp = cfp; bcfp != null; bcfp = bcfp.next) {
        if (bcfp.status == Config.Status.COMPLETE) continue;
        if (bcfp.dot >= bcfp.rp.nrhs) continue;
        bsp = bcfp.rp.rhs[bcfp.dot];
        if (bsp != sp) continue;
        bcfp.status = Config.Status.COMPLETE;
        new_ = Configlist.addbasis(bcfp.rp, bcfp.dot + 1);
        Config n = new_;
        Plink.add(new Addr<>(() -> n.bplp, v -> n.bplp = v), bcfp);
      }
      newstp = getstate(lemp);
      Action.add(new Addr<>(() -> stp.ap, v -> stp.ap = v), Action.Type.SHIFT, sp, newstp);
    }
  }

  public static void findLinks(Lemon lemp) {
    int i;
    Config cfp, other;
    State stp;
    Plink plp;
    for (i = 0; i < lemp.nstate; i++) {
      stp = lemp.sorted[i];
      for (cfp = stp.cfp; cfp != null; cfp = cfp.next) {
        cfp.stp = stp;
      }
    }
    for (i = 0; i < lemp.nstate; i++) {
      stp = lemp.sorted[i];
      for (cfp = stp.cfp; cfp != null; cfp = cfp.next) {
        for (plp = cfp.bplp; plp != null; plp = plp.next) {
          other = plp.cfp;
          Config o = other;
          Plink.add(new Addr<>(() -> o.fplp, v -> o.fplp = v), cfp);
        }
      }
    }
  }

  public static void findFollowSets(Lemon lemp) {
    int i;
    Config cfp;
    Plink plp;
    int progress;
    int change;
    for (i = 0; i < lemp.nstate; i++) {
      for (cfp = lemp.sorted[i].cfp; cfp != null; cfp = cfp.next) {
        cfp.status = Config.Status.INCOMPLETE;
      }
    }
    do {
      progress = 0;
      for (i = 0; i < lemp.nstate; i++) {
        for (cfp = lemp.sorted[i].cfp; cfp != null; cfp = cfp.next) {
          if (cfp.status == Config.Status.COMPLETE) continue;
          for (plp = cfp.fplp; plp != null; plp = plp.next) {
            change = Set.union(plp.cfp.fws, cfp.fws);
            if (change != 0) {
              plp.cfp.status = Config.Status.INCOMPLETE;
              progress = 1;
            }
          }
          cfp.status = Config.Status.COMPLETE;
        }
      }
    } while (progress != 0);
  }

  public static void findActions(Lemon lemp) {
    int i, j;
    Config cfp;
    State stp;
    Symbol sp;
    Rule rp;

    for (i = 0; i < lemp.nstate; i++) {
      stp = lemp.sorted[i];
      for (cfp = stp.cfp; cfp != null; cfp = cfp.next) {
        if (cfp.rp.nrhs == cfp.dot) {
          for (j = 0; j < lemp.nterminal; j++) {
            if (Set.find(cfp.fws, j) != 0) {
              State s = stp;
              Action.add(new Addr<>(() -> s.ap, v -> s.ap = v), Action.Type.REDUCE, lemp.symbols[j], cfp.rp);
            }
          }
        }
      }
    }
    if (lemp.start != null) {
      sp = Symbol.find(lemp.start);
      if (sp == null) sp = lemp.rule.lhs;
    } else {
      sp = lemp.rule.lhs;
    }
    State s = lemp.sorted[0];
    Action.add(new Addr<>(() -> s.ap, v -> s.ap = v), Action.Type.ACCEPT, sp, null);
    for (i = 0; i < lemp.nstate; i++) {
      Action ap, nap;
      stp = lemp.sorted[i];
      Assert.assertTrue(stp.ap != null);
      stp.ap = Action.sort(stp.ap);
      for (ap = stp.ap; ap != null && ap.next != null; ap = nap) {
        for (nap = ap.next; nap != null && nap.sp == ap.sp; nap = nap.next) {
          lemp.nconflict += resolveConfict(ap, nap, lemp.errsym);
        }
      }
    }
    for (rp = lemp.rule; rp != null; rp = rp.next) rp.canReduce = Boolean.FALSE;
    for (i = 0; i < lemp.nstate; i++) {
      Action ap;
      for (ap = lemp.sorted[i].ap; ap != null; ap = ap.next) {
        if (ap.type == Action.Type.REDUCE) ap.x.rp.canReduce = Boolean.TRUE;
      }
    }
    for (rp = lemp.rule; rp != null; rp = rp.next) {
      if (rp.canReduce) continue;
      Error.msg(lemp.filename, rp.ruleline, "This rule can not be reduced.\n");
      lemp.errorcnt++;
    }
  }

  public static int resolveConfict(Action apx, Action apy, Symbol errsym) {
    Symbol spx, spy;
    int errcnt = 0;
    Assert.assertTrue(apx.sp == apy.sp);
    if (apx.type == Action.Type.SHIFT && apy.type == Action.Type.REDUCE) {
      spx = apx.sp;
      spy = apy.x.rp.precsym;
      if (spy == null || spx.prec < 0 || spy.prec < 0) {
        apy.type = Action.Type.CONFLICT;
        errcnt++;
      } else if (spx.prec > spy.prec) {
        apy.type = Action.Type.RD_RESOLVED;
      } else if (spx.prec < spy.prec) {
        apx.type = Action.Type.SH_RESOLVED;
      } else if (spx.assoc == Symbol.Assoc.RIGHT) {
        apy.type = Action.Type.RD_RESOLVED;
      } else if (spx.assoc == Symbol.Assoc.LEFT) {
        apx.type = Action.Type.SH_RESOLVED;
      } else {
        Assert.assertTrue(spx.assoc == Symbol.Assoc.NONE);
        apy.type = Action.Type.CONFLICT;
        errcnt++;
      }
    } else if (apx.type == Action.Type.REDUCE && apy.type == Action.Type.REDUCE) {
      spx = apx.x.rp.precsym;
      spy = apy.x.rp.precsym;
      if (spx == null || spy == null || spx.prec < 0 || spy.prec < 0 || spx.prec == spy.prec) {
        apy.type = Action.Type.CONFLICT;
        errcnt++;
      } else if (spx.prec > spy.prec) {
        apy.type = Action.Type.RD_RESOLVED;
      } else {
        apx.type = Action.Type.RD_RESOLVED;
      }
    } else {
      //    /* Can't happen.  Shifts have to come before Reduces on the
      //    ** list because the reduces were added last.  Hence, if apx->type==REDUCE
      //    ** then it is impossible for apy->type==SHIFT */
    }
    return errcnt;
  }
}
