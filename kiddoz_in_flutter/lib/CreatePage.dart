import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import 'CreateForm.dart';

class CreatePage extends StatefulWidget {
  const CreatePage({Key? key}) : super(key: key);

  @override
  State<CreatePage> createState() => _CreatePageState();
}

class _CreatePageState extends State<CreatePage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: NestedScrollView(
          headerSliverBuilder: (BuildContext context, bool innerBoxIsScrolled) {
            return <Widget>[
              const CupertinoSliverNavigationBar(
                largeTitle: Text("Kiddoz"),
              )
            ];
          },
          body: const SingleChildScrollView(child: CreateForm())),
    );
  }
}
