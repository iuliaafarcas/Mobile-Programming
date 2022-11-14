import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:kiddoz_in_flutter/utils/Recommendation.dart';

import 'CreateForm.dart';
import 'UpdateForm.dart';

class UpdatePage extends StatefulWidget {
  final Recommendation recommendation;

  const UpdatePage({Key? key, required this.recommendation}) : super(key: key);

  @override
  State<UpdatePage> createState() => _UpdatePageState();
}

class _UpdatePageState extends State<UpdatePage> {
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
          body: SingleChildScrollView(
              child: UpdateForm(recommendation: widget.recommendation))),
    );
  }
}