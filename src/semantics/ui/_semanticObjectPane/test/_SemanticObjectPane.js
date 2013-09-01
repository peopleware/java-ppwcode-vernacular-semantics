define(["ppwcode.contracts/doh", "require"],
  function(doh, require) {

    doh.register("test _SemanticObjectPane Person",
                 require.toUrl("./_SemanticObjectPane_Person.html"), 999999);
    doh.register("test _SemanticObjectPane SpecialPerson",
      require.toUrl("./_SemanticObjectPane_SpecialPerson.html"), 999999);

  }
);
